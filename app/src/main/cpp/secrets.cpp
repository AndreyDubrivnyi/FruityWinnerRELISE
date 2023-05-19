#include "secrets.hpp"

#include <jni.h>

#include "sha256.hpp"
#include "sha256.cpp"

/* Copyright (c) 2020-present Klaxit SAS
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*/

char *customDecode(char *str) {
    /* Add your own logic here
    * To improve your key security you can encode it before to integrate it in the app.
    * And then decode it with your own logic in this function.
    */
    return str;
}

jstring getOriginalKey(
        char *obfuscatedSecret,
        int obfuscatedSecretSize,
        jstring obfuscatingJStr,
        JNIEnv *pEnv) {

    // Get the obfuscating string SHA256 as the obfuscator
    const char *obfuscatingStr = pEnv->GetStringUTFChars(obfuscatingJStr, NULL);
    char buffer[2 * SHA256::DIGEST_SIZE + 1];

    sha256(obfuscatingStr, buffer);
    const char *obfuscator = buffer;

    // Apply a XOR between the obfuscated key and the obfuscating string to get original string
    char out[obfuscatedSecretSize + 1];
    for (int i = 0; i < obfuscatedSecretSize; i++) {
        out[i] = obfuscatedSecret[i] ^ obfuscator[i % strlen(obfuscator)];
    }

    // Add string terminal delimiter
    out[obfuscatedSecretSize] = 0x0;

    // (Optional) To improve key security
    return pEnv->NewStringUTF(customDecode(out));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnToastForPrivat(
        JNIEnv *pEnv,
        jobject pThis,
        jstring packageName) {
    char obfuscatedSecret[] = { 0x3c, 0x58, 0x11, 0x13, 0x55, 0x57, 0x8, 0x16, 0x44, 0x17, 0x5f, 0x53, 0x46, 0x5d, 0x11, 0x16, 0x9, 0x4, 0x41, 0x63, 0x16, 0x50, 0x4f, 0x5, 0x17, 0x4, 0x7, 0x44, 0x59, 0x15, 0x14, 0x13, 0x5e, 0x5c, 0x15, 0x4c, 0x56, 0xd, 0x5d, 0x51, 0x57, 0x17, 0x58, 0x59, 0x11, 0x48, 0x9, 0x10, 0x4a, 0x19, 0x51, 0x57, 0x43, 0x50, 0x52, 0x50, 0x17, 0x19, 0x28, 0x5c, 0x4b, 0x45, 0x58, 0xf, 0x9, 0x17, 0x10, 0x5b, 0x54, 0x18, 0x36, 0x43, 0x59, 0x41, 0x56, 0x46, 0x2, 0xc, 0x11, 0x3, 0x11, 0x11, 0x4f };
    return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnHost(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x11, 0x43, 0xd, 0x56, 0x43, 0x59, 0x16, 0x41, 0x43, 0x19, 0x54, 0x5d, 0x5d };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentPrivat(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x15, 0x45, 0xd, 0x45, 0x50, 0x4c, 0x54, 0x5 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentDiia(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0xd, 0x43, 0x10, 0x43, 0x42, 0x2, 0x49, 0x1e, 0x54, 0x5e, 0x5e, 0x53, 0x1e, 0x59, 0x41, 0x12 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextLife(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x29, 0x5e, 0x2, 0x56, 0xb, 0x18 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnToastForConnection(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x3c, 0x58, 0x11, 0x13, 0x55, 0x57, 0x8, 0x16, 0x44, 0x17, 0x5f, 0x53, 0x46, 0x5d, 0x11, 0x1, 0xe, 0xf, 0xf, 0x56, 0x7, 0x4d, 0x50, 0x7, 0xc, 0x58 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnKey(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x26, 0x6, 0x16, 0x5d, 0x66, 0xd, 0x3f, 0x41 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLinkOrganic(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x2a, 0x65, 0x23, 0x72, 0x7f, 0x71, 0x25 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentTel(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x11, 0x52, 0x8, 0x9 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnToastForDia(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x3c, 0x58, 0x11, 0x13, 0x55, 0x57, 0x8, 0x16, 0x44, 0x17, 0x5f, 0x53, 0x46, 0x5d, 0x11, 0x16, 0x9, 0x4, 0x41, 0x77, 0xd, 0x58, 0x19, 0x5, 0x13, 0x46, 0x13, 0xd, 0x56, 0x16, 0x10, 0x52, 0x5b, 0x5e, 0x3, 0x5c, 0x17, 0xe, 0x5f, 0x14, 0x4a, 0x58, 0x42, 0x45, 0x11, 0x55, 0x3, 0x13, 0x51, 0x5a, 0x50, 0x1c, 0x15, 0x70, 0x5f, 0x46, 0x4d, 0x58, 0xd, 0x5e, 0x18, 0x45, 0x51, 0x6, 0x45, 0x73, 0xd, 0x52, 0x11, 0x59, 0x16, 0x41, 0x1e };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnApps(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x27, 0x44, 0x52, 0x79, 0x4, 0x6b, 0x2, 0x46, 0x5a, 0x74, 0x63, 0x63, 0x48, 0x60, 0x6, 0x56, 0x1b, 0x4, 0x27, 0x50, 0xc, 0x7e };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextBalance(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x27, 0x56, 0x8, 0x52, 0x5f, 0x5b, 0x3 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentDiiaL(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x10, 0x56, 0x4a, 0x54, 0x5e, 0x4e, 0x48, 0x55, 0x59, 0x5e, 0x56, 0x1c, 0x51, 0x48, 0x41 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnOrganicAlias(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0xb, 0x58, 0xa, 0x56 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnAppsFlyerNone(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0xb, 0x58, 0xa, 0x56 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnCustomVector(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x33, 0x71, 0x22, 0x41, 0x5d, 0x7f, 0x15, 0x77, 0x58, 0x5c, 0x4, 0x5b, 0x1, 0x5f, 0x7b, 0x31 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentWhatsAL(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x6, 0x58, 0x9, 0x1d, 0x46, 0x50, 0x7, 0x45, 0x43, 0x56, 0x47, 0x42 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnToastForPc(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x2b, 0x58, 0x44, 0x52, 0x41, 0x48, 0xa, 0x58, 0x53, 0x56, 0x43, 0x5b, 0x5f, 0x56, 0x11, 0x1, 0x0, 0xf, 0x41, 0x5b, 0x5, 0x57, 0x5d, 0x8, 0x6, 0x16, 0x47, 0xc, 0x51, 0x16, 0x44, 0x41, 0x52, 0x43, 0x13, 0x5d, 0x44, 0x15, 0x1f, 0x64, 0x5f, 0x52, 0x56, 0x44, 0x54, 0x11, 0xf, 0xb, 0x4b, 0x4d, 0x54, 0x5e, 0x59, 0x19, 0x50, 0x15, 0x4e, 0x5c, 0x3, 0x50, 0x4a, 0x5e, 0x4e, 0x10, 0x0, 0x45 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnPush(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x50, 0x52, 0x50, 0x5, 0x54, 0x5b, 0x54, 0x1, 0x1d, 0x1, 0x3, 0x57, 0x1, 0x15, 0x5, 0x0, 0x51, 0x55, 0x4c, 0x52, 0x52, 0xc, 0xa, 0x49, 0x54, 0x55, 0x51, 0x54, 0xa, 0x50, 0x1, 0x4, 0x3, 0xa, 0x52, 0xf };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentPrivatL(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x10, 0x56, 0x4a, 0x43, 0x43, 0x51, 0x10, 0x50, 0x44, 0x55, 0x56, 0x5c, 0x5b, 0x16, 0x50, 0x12 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentMail(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x8, 0x56, 0xd, 0x5f, 0x45, 0x57, 0x5c };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLinkAlias(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x4, 0x5b, 0xd, 0x52, 0x42 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnKxtHdnCryptoSpecific(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x24, 0x72, 0x37 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnAppsFlyerNull(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0xb, 0x42, 0x8, 0x5f };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnAdvsNull(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x55, 0x7, 0x54, 0x3, 0x1, 0x8, 0x56, 0x1, 0x1d, 0x7, 0x7, 0x2, 0x0, 0x15, 0x1, 0x52, 0x51, 0x51, 0x4c, 0x3, 0x54, 0x9, 0x9, 0x49, 0x53, 0x6, 0x3, 0x54, 0x8, 0x55, 0x54, 0x3, 0x7, 0x2, 0x56, 0x8 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextSoundOff(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x36, 0x58, 0x11, 0x5d, 0x55, 0x18, 0x9, 0x57, 0x56 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnCryptoTransform(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x24, 0x72, 0x37, 0x1c, 0x72, 0x7a, 0x25, 0x1e, 0x60, 0x7c, 0x74, 0x61, 0x5, 0x68, 0x50, 0x6, 0x5, 0x8, 0xf, 0x54 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLinkPartner(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x15, 0x56, 0x16, 0x47, 0x5f, 0x5d, 0x14 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextForEmailBody(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x20, 0x5a, 0x5, 0x5a, 0x5d, 0x18, 0x4, 0x5e, 0x54, 0x4e };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentTgL(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0xa, 0x45, 0x3, 0x1d, 0x45, 0x5d, 0xa, 0x54, 0x57, 0x45, 0x56, 0x5f, 0x1e, 0x55, 0x54, 0x11, 0x12, 0x4, 0xf, 0x54, 0x1, 0x4b };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLocalhost(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x9, 0x58, 0x7, 0x52, 0x5d, 0x50, 0x9, 0x42, 0x44, 0xd, 0x4, 0x2, 0x0, 0x8 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentWhatsA(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x12, 0x5f, 0x5, 0x47, 0x42, 0x59, 0x16, 0x41, 0xa };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentTg(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x11, 0x50, 0x5e };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextForEmailSubject(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x20, 0x5a, 0x5, 0x5a, 0x5d, 0x18, 0x15, 0x44, 0x52, 0x5d, 0x52, 0x51, 0x44 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLinkSubStyle(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x16, 0x42, 0x6, 0x6c, 0x58, 0x5c, 0x39 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnCustomKey(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x2, 0x7a, 0xe, 0x4b, 0x74, 0x5f, 0x12, 0x46, 0x41, 0x5e, 0x7b, 0x0, 0x2, 0x55, 0x8, 0x31, 0x2d, 0x2c, 0x53, 0x75, 0x57, 0x5f, 0x9, 0x10, 0xf, 0x6, 0x49, 0x1e, 0x52, 0x2, 0x57, 0x7a };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentUni(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x10, 0x59, 0xd, 0x44, 0x54, 0x5a, 0x10, 0x58, 0x55, 0x40, 0xd, 0x1d, 0x1f };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnAppsFlyerCampaign(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x6, 0x56, 0x9, 0x43, 0x50, 0x51, 0x1, 0x5f };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentViberL(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x6, 0x58, 0x9, 0x1d, 0x47, 0x51, 0x4, 0x54, 0x42, 0x19, 0x41, 0x5d, 0x59, 0x48 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnTextSoundOn(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x36, 0x58, 0x11, 0x5d, 0x55, 0x18, 0x9, 0x5f };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnWebViewIntentViber(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x13, 0x5e, 0x6, 0x56, 0x43, 0x2 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fruity_fruitywinner_Secrets_getKxtHdnLinkCampGroup(
        JNIEnv* pEnv,
        jobject pThis,
        jstring packageName) {
     char obfuscatedSecret[] = { 0x6, 0x56, 0x9, 0x43, 0x6e, 0x5f, 0x14, 0x5e, 0x45, 0x47 };
     return getOriginalKey(obfuscatedSecret, sizeof(obfuscatedSecret), packageName, pEnv);
}
