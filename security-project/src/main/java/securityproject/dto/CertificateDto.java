package securityproject.dto;

import java.util.Date;

public class CertificateDto {
    public String email;
    public String password;
    public String givenName;
    public String surname;
    public String organization;
    public String orgUnit;
    public String country;
    public boolean owner;
    public String[] extensions;

    public Date startDate;
    public Integer duration = 1;
}
