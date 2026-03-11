package com.clideOffice.clideApp.common.ocr_project.sds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.sql.Timestamp;
import java.util.Date;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "clide_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClideUser {
 
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
 
    @Column(name = "user_first_name", nullable = false)
    private String userFirstName;
 
    @Column(name = "user_last_name")
    private String userLastName;
 
    @Column(name = "user_email", nullable = false)
    private String userEmail;
 
    @Column(name = "user_contact_number")
    private String userContactNumber;
 
    @Column(name = "user_role")
    private Integer userRole;
 
    @Column(name = "user_company_profile", nullable = false)
    private String userCompanyProfile;
 
    @Column(name = "user_company_id")
    private Integer userCompanyId;
 
    @Column(name = "user_status", nullable = false)
    private String userStatus;
 
    @Column(name = "user_password")
    private String userPassword;
 
    @Column(name = "user_login_status")
    private String userLoginStatus;
 
    @Column(name = "category")
    private Integer category;
 
    @Column(name = "category_option_id")
    private Integer categoryOptionId;
 
    @Column(name = "api_key")
    private String apiKey;
 
    @Column(name = "pic_link", columnDefinition = "varchar(250) default 'default.png'")
    private String picLink;
 
    @Column(name = "otp")
    private Integer otp;
 
    @Column(name = "api_key_creation_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private Timestamp apiKeyCreationTime;
 
    @Column(name = "creation_time",  columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp creationTime = new Timestamp(new Date().getTime());
 
    @Column(name = "set_password_resend_email_time")
    private Timestamp setPasswordResendEmailTime;
 
    @Column(name = "user_pic")
    private String userPic;
 
    @Column(name = "soft_delete", columnDefinition = "int default 0")
    private int softDelete;
 
    @Column(name = "safety_head_as_admin", columnDefinition = "int default 0")
    private int safetyHeadAsAdmin;
 
    @Column(name = "last_retry")
    private Timestamp lastRetry;
 
    @Column(name = "retry_count", columnDefinition = "int default 0")
    private int retryCount;
 
    @Column(name = "retry_time")
    private Timestamp retryTime;
 
    @Column(name = "empId")
    private String empId;
 
    @Column(name = "designationId")
    private Integer designationId;
 
    @Column(name = "registration_token")
    private String registrationToken;
 
    @Column(name = "device_id")
    private String deviceId;
 
    @Column(name = "password_reset_dateTime")
    private Timestamp passwordResetDateTime;
    
    @Column(name = "is_from_employee_master")
    private boolean isFromEmployeeMaster = false;
    
}