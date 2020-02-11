package yongs.temp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yongs.temp.dao.RefreshTokenRepository;
import yongs.temp.model.RefreshToken;
import yongs.temp.vo.User;

@Service
public class TokenService {
	@Autowired
    private JwtTokenProviderService service;
	@Autowired
    RefreshTokenRepository repo;
	
	public String create(User user) {
    	// access-token과 refresh-token 2개를 생성한다.
    	String refreshToken = service.createRefreshToken(user);
    	String accessToken = service.createAccessToken(user);
    	
    	// refresh-token은 email과 함께 mongodb에 저장하고
    	RefreshToken refreshModel = new RefreshToken();
    	refreshModel.setEmail(user.getEmail());
    	refreshModel.setTokenValue(refreshToken);
    	
    	// 기존에 해당 email의 refresh-token이 있으면 지우고 
    	repo.deleteByEmail(user.getEmail());
    	// refresh-token을 mongodb에 저장
    	repo.insert(refreshModel);
    	
		return accessToken;
	}
 
	public String recreate(User user) {
		// refresh-token을 가져와서 
		RefreshToken refreshToken = repo.findByEmail(user.getEmail());
		
		// 유효성을 체크해서 유효하지 않으면 "REFRESH-TOKEN-NOT-VALID", 유효하면 access-token 생성
		if(!service.validateToken(refreshToken.getTokenValue())) {
			// 해당 사용자의 refresh-token을 삭제한다.
	    	repo.deleteByEmail(user.getEmail());
			return "REFRESH-TOKEN-NOT-VALID";
		} else {
			return service.createAccessToken(user);
		}
	}
}
