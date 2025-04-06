package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Slf4j
@Service
public class OtpService {
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, new OtpData(otp, System.currentTimeMillis()));
        log.info("Generated OTP for {}: {}", email, otp); // Console logging the OTP
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpData otpData = otpStore.get(email);
        if (otpData == null) {
            return false;
        }

        // Check if OTP is expired
        if (System.currentTimeMillis() - otpData.timestamp > OTP_VALID_DURATION) {
            otpStore.remove(email);
            return false;
        }

        // Check if OTP matches
        boolean isValid = otpData.otp.equals(otp);
        if (isValid) {
            otpStore.remove(email); // Remove OTP once used
        }
        return isValid;
    }

    private static class OtpData {
        String otp;
        long timestamp;

        OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
} 