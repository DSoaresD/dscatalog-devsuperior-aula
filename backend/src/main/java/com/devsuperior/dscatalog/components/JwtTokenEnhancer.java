package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer{
	
	@Autowired
	private UserRepository userRepository;

	//entra do ciclo de vida do token e acrescenta os objetos adicionais passados.
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		//o authentication traz o nome do usuario que esta sendo pego com o getName()
		User user = userRepository.findByEmail(authentication.getName());
		
		//inserindo novos elementos no token
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		//para inserir no token é usado o tipo mais especico DefaultOAuth2AccessToken doque o accesssToken.Por isso o cast
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		
		token.setAdditionalInformation(map);
		
		//pode ser retornado tanto o token quanto o accessToken, o token é apenas um tipo mais especifico no qual possui o metodo setAdditionalInformation
		return accessToken;
	}

}
