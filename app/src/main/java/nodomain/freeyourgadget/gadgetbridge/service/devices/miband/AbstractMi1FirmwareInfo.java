package nodomain.freeyourgadget.gadgetbridge.service.devices.miband;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some helper methods for Mi1 and Mi1A firmware.
 */
public abstract class AbstractMi1FirmwareInfo extends AbstractMiFirmwareInfo {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMi1FirmwareInfo.class);

    private static final int MI1_FW_BASE_OFFSET = 1056;

    protected AbstractMi1FirmwareInfo(@NonNull byte[] wholeFirmwareBytes) {
        super(wholeFirmwareBytes);
    }

    @Override
    public int getFirmwareOffset() {
        return 0;
    }

    public int getFirmwareLength() {
        return wholeFirmwareBytes.length;
    }

    public int getFirmwareVersion() {
        return (wholeFirmwareBytes[getOffsetFirmwareVersionMajor()] << 24)
                | (wholeFirmwareBytes[getOffsetFirmwareVersionMinor()] << 16)
                | (wholeFirmwareBytes[getOffsetFirmwareVersionRevision()] << 8)
                | wholeFirmwareBytes[getOffsetFirmwareVersionBuild()];
    }

    private int getOffsetFirmwareVersionMajor() {
        return MI1_FW_BASE_OFFSET + 3;
    }

    private int getOffsetFirmwareVersionMinor() {
        return MI1_FW_BASE_OFFSET + 2;
    }

    private int getOffsetFirmwareVersionRevision() {
        return MI1_FW_BASE_OFFSET + 1;
    }

    private int getOffsetFirmwareVersionBuild() {
        return MI1_FW_BASE_OFFSET;
    }

    @Override
    protected boolean isGenerallySupportedFirmware() {
        if (!isSingleMiBandFirmware()) {
            LOG.warn("not a single firmware");
            return false;
        }
        try {
            int majorVersion = getFirmwareVersionMajor();
            if (majorVersion == getSupportedMajorVersion()) {
                return true;
            } else {
                LOG.info("Only major version " + getSupportedMajorVersion() + " is supported: " + majorVersion);
            }
        } catch (IllegalArgumentException ex) {
            LOG.warn("invalid firmware or bug: " + ex.getLocalizedMessage(), ex);
        } catch (IndexOutOfBoundsException ex) {
            LOG.warn("not supported firmware: " + ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    protected abstract int getSupportedMajorVersion();
}
