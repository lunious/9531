    package com.lubanjianye.biaoxuntong.bean;

    import java.io.Serializable;



    public class JsonString implements Serializable{

        private String lxId = null;
        private String dlId = null;
        private String xlId = null;
        private String zyId = null;
        private String djId = null;
        private String fs = null;
        private String zcd = null;
        private String provinceCode = null;
        private String entrySign = null;

        public String getLxId() {
            return lxId;
        }

        public void setLxId(String lxId) {
            this.lxId = lxId;
        }

        public String getDlId() {
            return dlId;
        }

        public void setDlId(String dlId) {
            this.dlId = dlId;
        }

        public String getXlId() {
            return xlId;
        }

        public void setXlId(String xlId) {
            this.xlId = xlId;
        }

        public String getZyId() {
            return zyId;
        }

        public void setZyId(String zyId) {
            this.zyId = zyId;
        }

        public String getDjId() {
            return djId;
        }

        public void setDjId(String djId) {
            this.djId = djId;
        }

        public String getFs() {
            return fs;
        }

        public void setFs(String fs) {
            this.fs = fs;
        }

        public String getZcd() {
            return zcd;
        }

        public void setZcd(String zcd) {
            this.zcd = zcd;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getEntrySign() {
            return entrySign;
        }

        public void setEntrySign(String entrySign) {
            this.entrySign = entrySign;
        }
    }
