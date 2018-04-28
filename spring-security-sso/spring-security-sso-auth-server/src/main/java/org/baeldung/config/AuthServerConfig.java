package org.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	public static final String RESOURCE_ID = "test";

	@Autowired
    private AuthenticationManager authenticationManager;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore( new InMemoryTokenStore() ).authenticationManager( authenticationManager );
	}

	@Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }

    @Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception { // @formatter:off
        clients.inMemory()
            .withClient("SampleClientId")
            .secret("secret")
            .authorizedGrantTypes("authorization_code")
            .scopes("user_info")
        .and()
        .withClient("clientapp")
            .secret("123456")
            .authorizedGrantTypes("password")
            .scopes("read", "write")
            .resourceIds(RESOURCE_ID)
        .and()
        .withClient("clientcred")
            .secret("123456")
            .authorizedGrantTypes("client_credentials")
            .scopes("trust")
            .resourceIds(RESOURCE_ID)
        .and()
        .withClient("clientauthcode")
            .secret("123456")
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .scopes("read", "write")
            .resourceIds(RESOURCE_ID)
        .accessTokenValiditySeconds(3600)
        .autoApprove(true);
	} // @formatter:on


}
