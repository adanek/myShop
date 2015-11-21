package team1.myshop.web;

import org.apache.logging.log4j.Logger;
import team1.myshop.contracts.IAuthenticationService;
import team1.myshop.contracts.IDataHandler;
import team1.myshop.contracts.IHttpService;
import team1.myshop.core.ServiceLocator;
import team1.myshop.web.helper.AuthenticationService;
import team1.myshop.web.helper.HttpService;


public abstract class ServiceBase {
    protected IDataHandler dh;
    protected IHttpService http;
    protected IAuthenticationService auth;
    protected Logger logger;

    public ServiceBase() {
    }

    /**
     * Set the HttpService used by this class
     *
     * @param http the instance to set
     */
    public void setHttpService(IHttpService http) {
        this.http = http;
    }

    /**
     * Sets the Datahandler used by this class
     *
     * @param dh the instance to use
     */
    public void setDataHandler(IDataHandler dh) {
        this.dh = dh;
    }

    /**
     * Sets the AuthenticationService for this instance
     *
     * @param auth the instance to use in this object
     */
    public void setAuthenticationService(IAuthenticationService auth) {
        this.auth = auth;
    }

    /**
     * Sets the Logger for this instance
     *
     * @param logger the instance to use in this object
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public abstract void initializeLogger();

    /**
     * Sets the default values for the helper classes if not yet set
     */
    protected void initialize() {
        this.auth = this.auth == null ? new AuthenticationService() : this.auth;
        this.http = this.http == null ? new HttpService() : this.http;
        this.dh = this.dh == null ? ServiceLocator.getDataHandler() : this.dh;

        if (this.logger == null) {
            initializeLogger();
        }
    }
}
