// Copyright 2010 Google Inc. All Rights Reserved.

package me.ckhd.opengame.online.handle.common.google;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Security-related methods. For a secure implementation, all of this code
 * should be implemented on a server that communicates with the
 * application on the device. For the sake of simplicity and clarity of this
 * example, this code is included here and is executed on the device. If you
 * must verify the purchases on the phone, you should obfuscate this code to
 * make it harder for an attacker to replace the code with stubs that treat all
 * purchases as verified.
 */
public class Security {
    private static Logger log = LoggerFactory.getLogger(Security.class);

    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * Verifies that the signature from the server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey public key associated with the developer account
     * @param signedData signed data from server
     * @param signature server signature
     * @return true if the data and signature match
     */
    public static boolean verify(String publicKey, String signedData, String signature) {
        log.info( "signature: " + signature);
        Signature sig;
        try {
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            PublicKey key = Security.generatePublicKey(publicKey);
            sig.initVerify(key);
            sig.update(signedData.getBytes());
            if (!sig.verify(Base64.decode(signature))) {
            	log.info( "Signature verification failed.");
                return false;
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
        	log.error( "NoSuchAlgorithmException.",e);
        } catch (InvalidKeyException e) {
        	log.error("Invalid key specification.",e);
        } catch (SignatureException e) {
        	log.error( "Signature exception.",e);
        } catch (Base64DecoderException e) {
        	log.error( "Base64 decoding failed.",e);
        }
        return false;
    }
    
    /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    public static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            log.error( "Invalid key specification.",e);
            throw new IllegalArgumentException(e);
        } catch (Base64DecoderException e) {
        	log.error( "Base64 decoding failed.",e);
            throw new IllegalArgumentException(e);
        }
    }
    
    public static void main(String[] args) {
        String str = "{\"validation\":\"{\\\"orderId\\\":\\\"GPA.3342-7837-1780-44364\\\",\\\"packageName\\\":\\\"com.szckhd.jwgly.azfanti\\\",\\\"productId\\\":\\\"300yb\\\",\\\"purchaseTime\\\":1498647972322,\\\"purchaseState\\\":0,\\\"developerPayload\\\":\\\"yd17062800061863;http:\\/\\/ol.haifurong.cn\\/netpay\\/callback\\/googleplay\\/1.1.0\\/\\\",\\\"purchaseToken\\\":\\\"iadcoedgbnopmabpiocgflbk.AO-J1Owl1xXgvFhfrjeV_v5fsyahSgHW6H0kHM_lLF84mQCDTgmZ7TSHwlHOSjRJC0nAcuUolCdwo4DL67rDscDn0l_fLy2obKv8NvkAYBvlX30_vH9H4dQ\\\"}\",\"sign\":\"TCZNY8bzZ6wwcxL0m9VU/Uy98iP4BFvisxIeYU1Dglje8kBSPtXIGnjMjPK/1TEZTa+fCANGAM9HuoPMcluuSRi3vbbOkNPdJjvWidZPokmOiqLgl7/2GyrYy0O3iF1ayXYpbabR2aNZWrNx1HNM/KCcbgUuQgOJvZxuqQseshuhHHZS0/F6I0Oaj8OTfZ4pgrQwgunc09fPpyTG9cSQH0/OEpr5mj/Yoit8Yezpi/lATzGUfcT+/s0ea/kcT8LckY4Pivfy7awonzhbv77UAkC5Nq/SwpQFqzisBu5GozlR2u0SHq3zLvpuatHJNYY9DB5sOItXjKnhcHghJ0HyGA==\"}";
        String public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkOaWWOSnqhlxznjN25r9DpogtoEljo1lv2IanQDqsaeLS/EyEw6h/9Y1HBTPgc3/Xjt3UVsa6sTguSpQr/aCHipH7UvxcvkdI/l5LL6shseTmuR1mFSOZU0mQZcROonDHE8H+QePjk0S3yDzgGMnc7M2leogT8uc+CKYmwTgAXr4M43b0ECFwOF906CDmpj7xWiZm1DIS2aLcfMMhdlCWG14c7qg4CxlRkyaIbfIZT0C3ZaLDpHIvWt1KTcf516LZU9Y2+XVRcRWEJ5fKoX9abBdC+PmWa0q7TTNRSOil/M5gXemBSYteORu756zuN+jN219okQGA3L6MgfeNOLKLQIDAQAB";
        JSONObject json = JSONObject.parseObject(str);
        System.out.println(json.toJSONString());
        System.out.println(json.get("sign"));
        verify(public_key, json.getString("validation"), json.getString("sign"));
    }
}
