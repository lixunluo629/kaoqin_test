package com.alibaba.excel.write.metadata.fill;

import com.alibaba.excel.enums.WriteDirectionEnum;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/fill/FillConfig.class */
public class FillConfig {
    private WriteDirectionEnum direction;
    private Boolean forceNewRow;
    private boolean hasInit;

    public WriteDirectionEnum getDirection() {
        return this.direction;
    }

    public void setDirection(WriteDirectionEnum direction) {
        this.direction = direction;
    }

    public Boolean getForceNewRow() {
        return this.forceNewRow;
    }

    public void setForceNewRow(Boolean forceNewRow) {
        this.forceNewRow = forceNewRow;
    }

    public void init() {
        if (this.hasInit) {
            return;
        }
        if (this.direction == null) {
            this.direction = WriteDirectionEnum.VERTICAL;
        }
        if (this.forceNewRow == null) {
            this.forceNewRow = Boolean.FALSE;
        }
        this.hasInit = true;
    }

    public static FillConfigBuilder builder() {
        return new FillConfigBuilder();
    }

    /* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/write/metadata/fill/FillConfig$FillConfigBuilder.class */
    public static class FillConfigBuilder {
        private FillConfig fillConfig = new FillConfig();

        FillConfigBuilder() {
        }

        public FillConfigBuilder direction(WriteDirectionEnum direction) {
            this.fillConfig.setDirection(direction);
            return this;
        }

        public FillConfigBuilder forceNewRow(Boolean forceNewRow) {
            this.fillConfig.setForceNewRow(forceNewRow);
            return this;
        }

        public FillConfig build() {
            return build(true);
        }

        public FillConfig build(boolean autoInit) {
            if (autoInit) {
                this.fillConfig.init();
            }
            return this.fillConfig;
        }
    }
}
