package com.wuwind.netlibrary.response;


import com.wuwind.netlibrary.Response;

/**
 * Created by hongfengwu on 2017/3/25.
 */

public class LoginResponse extends Response<LoginResponse.Content> {


    public static class Content {

        /**
         * principal : {"accountName":"15280091256","tenantId":1,"userId":11,"principalMobile":"15280091256","accountType":"TENANT","identity":"TENANT","tenantName":"云南顾赛维斯安全顾问服务有限公司","proxyName":"终端研发人员003","authentication":{"userId":11,"userName":"终端研发人员003","mobile":"15280091256","address":"终端研发人员003地址","position":"13619669949","activated":true,"builtin":false,"updateDateTime":"2017-09-06 11:39:04","createDateTime":"2017-09-06 11:39:04","verboses":{},"depId":9,"depName":"终端研发人员003"},"profile":"knight.view.frame.platform.profile.TenantUserProfile"}
         */

        private PrincipalBean principal;

        public PrincipalBean getPrincipal() {
            return principal;
        }

        public void setPrincipal(PrincipalBean principal) {
            this.principal = principal;
        }

        public static class PrincipalBean {
            /**
             * accountName : 15280091256
             * tenantId : 1
             * userId : 11
             * principalMobile : 15280091256
             * accountType : TENANT
             * identity : TENANT
             * tenantName : 云南顾赛维斯安全顾问服务有限公司
             * proxyName : 终端研发人员003
             * authentication : {"userId":11,"userName":"终端研发人员003","mobile":"15280091256","address":"终端研发人员003地址","position":"13619669949","activated":true,"builtin":false,"updateDateTime":"2017-09-06 11:39:04","createDateTime":"2017-09-06 11:39:04","verboses":{},"depId":9,"depName":"终端研发人员003"}
             * profile : knight.view.frame.platform.profile.TenantUserProfile
             */

            private String accountName;
            private int tenantId;
            private int userId;
            private String principalMobile;
            private String accountType;
            private String identity;
            private String tenantName;
            private String proxyName;
            private AuthenticationBean authentication;
            private String profile;

            public String getAccountName() {
                return accountName;
            }

            public void setAccountName(String accountName) {
                this.accountName = accountName;
            }

            public int getTenantId() {
                return tenantId;
            }

            public void setTenantId(int tenantId) {
                this.tenantId = tenantId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getPrincipalMobile() {
                return principalMobile;
            }

            public void setPrincipalMobile(String principalMobile) {
                this.principalMobile = principalMobile;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public String getIdentity() {
                return identity;
            }

            public void setIdentity(String identity) {
                this.identity = identity;
            }

            public String getTenantName() {
                return tenantName;
            }

            public void setTenantName(String tenantName) {
                this.tenantName = tenantName;
            }

            public String getProxyName() {
                return proxyName;
            }

            public void setProxyName(String proxyName) {
                this.proxyName = proxyName;
            }

            public AuthenticationBean getAuthentication() {
                return authentication;
            }

            public void setAuthentication(AuthenticationBean authentication) {
                this.authentication = authentication;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public static class AuthenticationBean {
                /**
                 * userId : 11
                 * userName : 终端研发人员003
                 * mobile : 15280091256
                 * address : 终端研发人员003地址
                 * position : 13619669949
                 * activated : true
                 * builtin : false
                 * updateDateTime : 2017-09-06 11:39:04
                 * createDateTime : 2017-09-06 11:39:04
                 * verboses : {}
                 * depId : 9
                 * depName : 终端研发人员003
                 */

                private int userId;
                private String userName;
                private String mobile;
                private String address;
                private String position;
                private boolean activated;
                private boolean builtin;
                private String updateDateTime;
                private String createDateTime;
                private VerbosesBean verboses;
                private int depId;
                private String depName;

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public boolean isActivated() {
                    return activated;
                }

                public void setActivated(boolean activated) {
                    this.activated = activated;
                }

                public boolean isBuiltin() {
                    return builtin;
                }

                public void setBuiltin(boolean builtin) {
                    this.builtin = builtin;
                }

                public String getUpdateDateTime() {
                    return updateDateTime;
                }

                public void setUpdateDateTime(String updateDateTime) {
                    this.updateDateTime = updateDateTime;
                }

                public String getCreateDateTime() {
                    return createDateTime;
                }

                public void setCreateDateTime(String createDateTime) {
                    this.createDateTime = createDateTime;
                }

                public VerbosesBean getVerboses() {
                    return verboses;
                }

                public void setVerboses(VerbosesBean verboses) {
                    this.verboses = verboses;
                }

                public int getDepId() {
                    return depId;
                }

                public void setDepId(int depId) {
                    this.depId = depId;
                }

                public String getDepName() {
                    return depName;
                }

                public void setDepName(String depName) {
                    this.depName = depName;
                }

                public static class VerbosesBean {
                }
            }
        }
    }

}
