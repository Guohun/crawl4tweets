/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author uqgzhu1
 */
public class TwoStepOAuth extends DefaultApi10a {
  public String getAccessTokenEndpoint() {
    return null;
  }

  public String getAuthorizationUrl(Token arg0) {
    return null;
  }

  public String getRequestTokenEndpoint() {
    return null;
  }

}
