package com.dvaren.bill.controller;

import com.dvaren.bill.config.ApiException;
import com.dvaren.bill.constants.SystemConstants;
import com.dvaren.bill.domain.entity.Activities;
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
import java.util.function.Function;


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

    @GetMapping
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
    public ResponseResult<Object> participateActivities(@NotBlank(message = "id不能为空") @RequestParam("id") String activityId, HttpServletRequest request) throws ApiException {
        String uid = JWTUtil.getUid(request.getHeader(SystemConstants.ACCESS_TOKEN));
        participantsService.addParticipant(activityId,uid);
        return ResponseResult.ok("加入成功",null);
    }
}
