package models.User;

/***
 * Class used to create JSONObjects with the gson Library. It resembles the Oracle database
 * table model.
 */
public class CustomerRecord {

    private int CUSTOMER_ID;
    private String CUSTOMER_FORENAME;
    private String CUSTOMER_SURNAME;
    private String USERNAME;
    private String PASSWORD;
    private String DATE_OF_BIRTH;
    private String POST_CODE;
    private String ADDRESS_LINE;

    public int getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID( int CUSTOMER_ID ) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getCUSTOMER_FORENAME() {
        return CUSTOMER_FORENAME;
    }

    public void setCUSTOMER_FORENAME( String CUSTOMER_FORENAME ) {
        this.CUSTOMER_FORENAME = CUSTOMER_FORENAME;
    }

    public String getCUSTOMER_SURNAME() {
        return CUSTOMER_SURNAME;
    }

    public void setCUSTOMER_SURNAME( String CUSTOMER_SURNAME ) {
        this.CUSTOMER_SURNAME = CUSTOMER_SURNAME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME( String USERNAME ) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD( String PASSWORD ) {
        this.PASSWORD = PASSWORD;
    }

    public String getDATE_OF_BIRTH() {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH( String DATE_OF_BIRTH ) {
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
    }

    public String getPOST_CODE() {
        return POST_CODE;
    }

    public void setPOST_CODE( String POST_CODE ) {
        this.POST_CODE = POST_CODE;
    }

    public String getADDRESS_LINE() {
        return ADDRESS_LINE;
    }

    public void setADDRESS_LINE( String ADDRESS_LINE ) {
        this.ADDRESS_LINE = ADDRESS_LINE;
    }
}
