package io.deepstory.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.deepstory.exception.BadRequestException;
import io.deepstory.exception.ServerErrorRequestException;
import io.deepstory.jwt.Subject;
import io.deepstory.model.dto.SecretFriendAccountDTO;
import io.deepstory.model.dto.SecretFriendListDTO;
import io.deepstory.model.dto.SecretImageDTO;
import io.deepstory.model.dto.SecretPostDTO;
import io.deepstory.model.dto.SecretPostImageDTO;
import io.deepstory.model.dto.SecretPostListDTO;
import io.deepstory.model.entity.AccountEntity;
import io.deepstory.model.entity.SecretFriendsEntity;
import io.deepstory.model.entity.SecretImageEntity;
import io.deepstory.model.entity.SecretPostEntity;
import io.deepstory.model.repository.AccountRepository;
import io.deepstory.model.repository.SecretFreindsRecpository;
import io.deepstory.model.repository.SecretImageRepository;
import io.deepstory.model.repository.SecretPostRecpository;

@Service
public class SecretService {

	@Autowired
	private SecretFreindsRecpository secretFreindsRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SecretPostRecpository secretPostRecpository;

	@Autowired
	private SecretImageRepository secretImageRepository;

	public void notExistAccount(int accountId) throws Exception {

		boolean isExist = accountRepository.existsById(accountId);

		if (!isExist) {
			
			throw new ServerErrorRequestException("존재하지 않은 계정 입니다.");
		}
	}

	public void notExistPost(int postId) throws Exception {

		boolean isExist = secretPostRecpository.existsById(postId);

		if (!isExist) {
			
			throw new ServerErrorRequestException("게시글이 존재 하지 않습니다.");
		}
	}

	@Transactional
	public boolean secretReqeust(int hostId, String guestEmail, String secretBoard) throws Exception {
		
		AccountEntity host = accountRepository.findById(hostId).get();
		AccountEntity guest = accountRepository.findByAccountEmail(guestEmail).get();
		
		SecretFriendsEntity secretFriends = SecretFriendsEntity.builder().secretBoard(secretBoard).state("신청상태")
				.hostId(host).guestId(guest).build();
		
		SecretFriendsEntity secretResult = secretFreindsRepository.save(secretFriends);
		
		if (secretResult == null) {
			
			return false;
		}
		return true;
	}

	@Transactional
	public ArrayList<SecretFriendAccountDTO> secretAlarm(int guestId) throws Exception {

		AccountEntity guest = accountRepository.findById(guestId).get();
		List<SecretFriendsEntity> secretList = secretFreindsRepository.findAllByGuestId(guest);

		ArrayList<SecretFriendAccountDTO> friendAccountList = new ArrayList<SecretFriendAccountDTO>();

		secretList.stream().forEach(s -> friendAccountList.add(new SecretFriendAccountDTO(s.getSecretFriendId(),
				s.getHostId().getAccountId(), s.getHostId().getAccountEmail(), s.getHostId().getAccountName())));

		return friendAccountList;

	}

	@Transactional
	public boolean secretAccept(int guestId, String hostEmail) throws Exception {
		
		AccountEntity host = accountRepository.findByAccountEmail(hostEmail).get();
		AccountEntity guest = accountRepository.findById(guestId).get();
		
		secretFreindsRepository.updateStateByHostIdAndGuestId(host, guest);
		
		return true;
		
	}

	@Transactional
	public ArrayList<SecretFriendListDTO> getSecretFriend(int accountId) throws Exception {

		AccountEntity account = accountRepository.findById(accountId).get();
		
		List<SecretFriendsEntity> guestFriend = null;
		List<SecretFriendsEntity> hostFriend = null;

		try {
			
			guestFriend = secretFreindsRepository.findGuestId(account);
			
		} catch (Exception e) {
			
			throw new BadRequestException("비밀 친구가 존재하지 않습니다.");
		}

		try {
			
			hostFriend = secretFreindsRepository.findHostId(account);
			
		} catch (Exception e) {
			
			throw new BadRequestException("비밀 친구가 존재하지 않습니다.");
		}

		ArrayList<SecretFriendListDTO> friendAccountList = new ArrayList<SecretFriendListDTO>();
		ArrayList<String> boardNames = new ArrayList<String>();
		
		guestFriend.stream().forEach(
				s -> friendAccountList.add(new SecretFriendListDTO(s.getSecretFriendId(), s.getHostId().getAccountId(),
						s.getHostId().getAccountEmail(), s.getHostId().getAccountName(), s.getSecretBoard())));
		
		guestFriend.stream().forEach(a -> boardNames.add(a.getSecretBoard()));
		
		hostFriend.stream().forEach(
				s -> friendAccountList.add(new SecretFriendListDTO(s.getSecretFriendId(), s.getGuestId().getAccountId(),
						s.getGuestId().getAccountEmail(), s.getGuestId().getAccountName(), s.getSecretBoard())));

		return friendAccountList;
		
	}

	@Transactional
	public HashMap<String, SecretFriendAccountDTO> getSecretProfil(Subject account, int secretFriendId,
			String friendEmail) throws Exception {

		AccountEntity friend = accountRepository.findByAccountEmail(friendEmail).get();

		SecretFriendAccountDTO myAccount = new SecretFriendAccountDTO(secretFriendId, account.getAccountId(),
				account.getAccountEmail(), account.getAccountName());

		SecretFriendAccountDTO friendAccount = new SecretFriendAccountDTO(secretFriendId, friend.getAccountId(),
				friend.getAccountEmail(), friend.getAccountName());

		HashMap<String, SecretFriendAccountDTO> profil = new HashMap<String, SecretFriendAccountDTO>();

		profil.put("myAccount", myAccount);
		profil.put("friendAccount", friendAccount);

		return profil;

	}

	@Transactional
	public ArrayList<SecretPostListDTO> getSecretPostList(int secretFriendId) throws Exception {

		ArrayList<SecretPostListDTO> secretPostList = new ArrayList<SecretPostListDTO>() ;

		try {
			
			SecretFriendsEntity secretFriend = secretFreindsRepository.findById(secretFriendId).get();
			List<SecretPostEntity> secretPost = secretPostRecpository.findAllBySecretFriendId(secretFriend);
		
			List<SecretImageEntity> secretImageList = secretPost.stream()
					.map(p -> secretImageRepository.findImageName(p)).collect(Collectors.toList());
			
			for (int i = 0; i < secretPost.size(); i++) {
				
				if (secretImageList.get(i) != null) {
					
						secretPostList.add(new SecretPostListDTO(secretPost.get(i).getSecretPostId(),
								secretPost.get(i).getSecretPostName(), secretPost.get(i).getSecretPostContents(),
								secretPost.get(i).getSecretWriterId().getAccountId(), secretFriendId,
								secretImageList.get(i).getSecretImageName()));
				} else {
					
					secretPostList.add(new SecretPostListDTO(secretPost.get(i).getSecretPostId(),
							secretPost.get(i).getSecretPostName(), secretPost.get(i).getSecretPostContents(),
							secretPost.get(i).getSecretWriterId().getAccountId(), secretFriendId, null));
				}
			}
		} catch (Exception e) {
			
			throw new BadRequestException("게시글이 존재하지 않습니다.");
		}

		return secretPostList;
	}

	public Integer secretPostInsert(SecretPostDTO secretPostDTO) throws Exception {
		
		try {
			
			notExistAccount(secretPostDTO.getSecretWriterId());
			
			Optional<AccountEntity> accountId = accountRepository.findById(secretPostDTO.getSecretWriterId());
			Optional<AccountEntity> host = accountRepository.findById(secretPostDTO.getSecretFriendId());
			List<SecretFriendsEntity> secretFriend = secretFreindsRepository.findByHostId(host.get());
			
			SecretPostEntity secretPostEntity = new SecretPostEntity(secretPostDTO.getSecretPostId(), secretPostDTO.getSecretPostName(), secretPostDTO.getSecretPostContents(), secretFriend.get(0),
					 accountId.get());

			SecretPostEntity secretPost = secretPostRecpository.save(secretPostEntity);

			return secretPost.getSecretPostId();
			
		} catch (Exception e) {
			
			throw new ServerErrorRequestException("해당 게시글에 저장을 실패하였습니다.");
		}

	}
	
	@Transactional
	public Integer addSecretImage(SecretImageDTO newImage) throws Exception {
		
		notExistPost(newImage.getSecretPostId());
		
		Optional<SecretPostEntity> secretPost = secretPostRecpository.findById(newImage.getSecretPostId());

		SecretImageEntity imageEntity = SecretImageEntity
				.builder().secretImageName(newImage.getSecretImageName())
				.secretPostId(secretPost.get()).build();

		SecretImageEntity image = secretImageRepository.save(imageEntity);

		if (image != null) {
			
			return image.getSecretImageId();
		}
		return null;
	}
	
	@Transactional
	public SecretPostDTO getSecretPost(int secretPostId) throws Exception {
		
		notExistPost(secretPostId);

		SecretPostEntity secretPostEntity = secretPostRecpository.findById(secretPostId).get();

		SecretPostDTO secretPostDTO = SecretPostDTO.builder().secretPostId(secretPostId).secretPostName(secretPostEntity.getSecretPostName())
				.secretPostContents(secretPostEntity.getSecretPostContents())
				.secretFriendId(secretPostEntity.getSecretFriendId().getSecretFriendId()).secretWriterId(secretPostEntity.getSecretWriterId().getAccountId()).build();

		return secretPostDTO;
		
	}
	
	@Transactional
	public String getSecretImage(int postId) throws Exception {

		Optional<SecretPostEntity> secretPostEntity = secretPostRecpository.findById(postId);
		String imageName = secretImageRepository.findImageName(secretPostEntity.get()).getSecretImageName();

		return imageName;
		
	}
	
	@Transactional
	public Integer updateSecretPost(SecretPostImageDTO secretPost) throws Exception {

		SecretPostEntity secretPostEntity = secretPostRecpository.findById(secretPost.getSecretPostId()).get();
		SecretImageEntity secretImageEntity = secretImageRepository.findImageName(secretPostEntity);

		secretPostEntity.setSecretPostName(secretPost.getSecretTitle());
		secretPostEntity.setSecretPostContents(secretPost.getSecretContents());
		secretImageEntity.setSecretImageName(secretPost.getSecretImage().get(0).get("name"));

		SecretPostEntity post = secretPostRecpository.save(secretPostEntity);
		SecretImageEntity image = secretImageRepository.save(secretImageEntity);

		return post.getSecretPostId();
	}

	@Transactional
	public boolean deleteSecretPost(int secretPostId) throws Exception {

		secretPostRecpository.deleteById(secretPostId);

		return true;
		
	}

}

