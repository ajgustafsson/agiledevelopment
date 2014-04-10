package se.chalmers.agile5.entities;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.util.EncodingUtils;

import java.net.HttpURLConnection;

import static org.eclipse.egit.github.core.client.IGitHubConstants.AUTH_TOKEN;

/**
 * Overwritten version of {@link org.eclipse.egit.github.core.client.GitHubClient} offering some more options
 * regarding getting credentials and tokens.
 *
 * @author Armin
 */
public class AgileGitHubClient extends GitHubClient {

    private String credentials;

    private String user;

    private String userAgent = USER_AGENT;

    public AgileGitHubClient() {
        super();
    }

    @Override
    protected HttpURLConnection configureRequest(final HttpURLConnection request) {
        if (credentials != null)
            request.setRequestProperty(HEADER_AUTHORIZATION, credentials);
        request.setRequestProperty(HEADER_USER_AGENT, userAgent);
        request.setRequestProperty(HEADER_ACCEPT,
                "application/vnd.github.beta+json"); //$NON-NLS-1$
        return request;
    }

    @Override
    public GitHubClient setUserAgent(String agent) {
        if (agent != null && agent.length() > 0)
            userAgent = agent;
        else
            userAgent = USER_AGENT;
        return this;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public GitHubClient setCredentials(String user, String password) {
        this.user = user;
        if (user != null && user.length() > 0 && password != null
                && password.length() > 0)
            credentials = "Basic " //$NON-NLS-1$
                    + EncodingUtils.toBase64(user + ':' + password);
        else
            credentials = null;
        return this;
    }

    @Override
    public GitHubClient setOAuth2Token(String token) {
        if (token != null && token.length() > 0){
            credentials = AUTH_TOKEN + ' ' + token;
        }
        else {
            credentials = null;
        }
        return this;
    }

    public String getCredentials(){
        return credentials;
    }

    public void setCredentials(String credentials){
        this.credentials = credentials;
    }
}
