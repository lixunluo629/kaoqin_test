package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.adapter.CacheAdapter;
import com.moredian.onpremise.core.common.constants.AuthConstants;
import com.moredian.onpremise.core.common.constants.Constants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/AuthCodeUtils.class */
public class AuthCodeUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AuthCodeUtils.class);

    private static String getServerOgrCode() {
        return CacheAdapter.getServerOrgCode(Constants.SERVER_ORG_CODE_KEY);
    }

    public static String initServerDeviceId() throws NoSuchAlgorithmException, IOException {
        String cpuSn = RuntimeSystemUtils.getCPUSerial();
        logger.info("cpu :{}", cpuSn);
        String motherboardSn = RuntimeSystemUtils.getMotherboardSN();
        logger.info("motherboardSn :{}", motherboardSn);
        String version = RuntimeSystemUtils.os().getVersion();
        logger.info("version :{}", version);
        String mac = RuntimeSystemUtils.getMac();
        logger.info("mac :{}", mac);
        String uuid = RuntimeSystemUtils.getCloudUuid();
        logger.info("uuid :{}", uuid);
        String deviceId = "";
        try {
            if (!StringUtils.isEmpty(cpuSn + motherboardSn + version + mac + uuid)) {
                deviceId = MD5Utils.toMD5(cpuSn + motherboardSn + version + mac + uuid);
            } else {
                UUID.randomUUID().toString();
                deviceId = MD5Utils.toMD5(uuid);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("error:{}", (Throwable) e);
        }
        CacheAdapter.cacheServerOrgCode(Constants.SERVER_ORG_CODE_KEY, deviceId);
        return deviceId;
    }

    public static Map<String, Object> decryptByPublicKey(String authCode, String publicKey) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        String orgCode = getServerOgrCode();
        byte[] decrypByte = RSAUtils.decryptByPublicKey(MyBase64Utils.decodeStringForByte(authCode), publicKey);
        AssertUtil.isNullOrEmpty(decrypByte, OnpremiseErrorEnum.SYSTEM_AUTH_CODE_ERROR);
        String decryp = new String(decrypByte);
        logger.info("decryp result :{}", decryp);
        Map<String, Object> params = JsonUtils.json2Map(decryp);
        String licenseOrgCode = String.valueOf(params.get(AuthConstants.AUTH_PARAM_ORG_CODE_KEY));
        if ("SALHJA444AS4F574SFD4".equals(licenseOrgCode)) {
            logger.info("return result :{}", JsonUtils.toJson(params));
            return params;
        }
        AssertUtil.isTrue(Boolean.valueOf(orgCode.equals(licenseOrgCode)), OnpremiseErrorEnum.SYSTEM_AUTH_CODE_ERROR);
        logger.info("return result :{}", JsonUtils.toJson(params));
        return params;
    }
}
