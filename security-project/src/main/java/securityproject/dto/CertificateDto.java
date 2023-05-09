package securityproject.dto;

import securityproject.model.Extension;

import java.util.Date;
import java.util.List;

public class CertificateDto {
    public String email;
    public String password;
    public String givenName;
    public String surname;
    public String organization;
    public String orgUnit;
    public String country;
    public boolean owner;
    public List<String> extensions;

    public Date startDate;
    public Integer duration = 1;
}
