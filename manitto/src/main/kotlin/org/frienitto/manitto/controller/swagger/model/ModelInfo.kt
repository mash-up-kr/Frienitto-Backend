package org.frienitto.manitto.controller.swagger.model

import org.frienitto.manitto.dto.*

class RoomDetailInfo : Response<RoomDto>()

class RoomListInfo: Response<List<RoomDto>>()

class VerifyCodeInfo : Response<RegisterToken>()

class SignUpInfo : Response<UserDto>()

class SignInInfo : Response<AccessToken>()

class MatchResultInfo : Response<MatchResultDto>()

class MatchListInfo : Response<List<MissionDto>>()