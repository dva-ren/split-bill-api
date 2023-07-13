package com.dvaren.bill.controller;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Activities;
import com.dvaren.bill.domain.entity.ActivityParticipants;
import com.dvaren.bill.service.ActivitiesService;
import com.dvaren.bill.service.ActivityParticipantsService;
import com.dvaren.bill.utils.JWTUtil;
import com.dvaren.bill.utils.ResponseResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/activities")
@Validated
public class ActivitiesController {

    @Resource
    private ActivitiesService activitiesService;

    @Resource
    private ActivityParticipantsService participantsService;

    @PostMapping
    public ResponseResult<Activities> create(@RequestBody Activities activities, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        activities.setCreatorId(uid);
        Activities activity = activitiesService.createActivity(activities);
        return ResponseResult.ok(activity);
    }

    @GetMapping("/detail")
    public ResponseResult<Map<String, List<Activities>>> list(HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        List<Activities> participant = activitiesService.getsJoinActivities(uid);
        List<Activities> created = activitiesService.getsTheCreatedActivities(uid);
        Map<String, List<Activities>> res = new HashMap<>();
        res.put("participant",participant);
        res.put("created",created);
        return ResponseResult.ok(res);
    }

    @PostMapping("/join")
    public ResponseResult<Object> participateActivities(@NotBlank(message = "活动id不能为空") @RequestParam("activityId") String activityId, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        participantsService.addParticipant(activityId,uid);
        return ResponseResult.ok("加入成功",null);
    }

    @PostMapping("/dissolution")
    public ResponseResult<Object> dissolutionActivity(@NotBlank(message = "活动id不能为空") @RequestParam("activityId") String activityId, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        activitiesService.dissolutionActivity(activityId,uid);
        return ResponseResult.ok("解散活动成功",null);
    }

    @GetMapping("/users")
    public ResponseResult<Object> participate(@NotBlank(message = "活动id不能为空") @RequestParam("activityId") String activityId) {
        List<ActivityParticipants> participant = participantsService.getParticipant(activityId);
        return ResponseResult.ok(participant);
    }

    @GetMapping
    public ResponseResult<List<Activities>> activities(HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        List<Activities> activities = activitiesService.getsJoinActivities(uid);
        return ResponseResult.ok(activities);
    }
}
