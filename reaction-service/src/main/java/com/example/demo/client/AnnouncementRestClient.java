package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.models.AnnouncementModel;


@FeignClient(name="ANNOUCEMENT-SERVICE")
public interface AnnouncementRestClient {

	@GetMapping("/announcements/host/{hostId}")
	List<AnnouncementModel> getAllAnnouncementProp(@PathVariable("hostId") Long hostId); 
}
