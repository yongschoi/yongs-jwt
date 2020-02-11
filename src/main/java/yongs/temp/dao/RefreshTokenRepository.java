package yongs.temp.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import yongs.temp.model.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
	public RefreshToken findByEmail(String email);
	public Long deleteByEmail(String email);
}