package org.frienitto.manitto.controller.auth

import io.swagger.annotations.*
import org.frienitto.manitto.controller.swagger.model.SignInInfo
import org.frienitto.manitto.controller.swagger.model.SignUpInfo
import org.frienitto.manitto.controller.swagger.model.VerifyCodeInfo
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.model.ErrorInfo
import org.frienitto.manitto.service.AuthService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1"])
@Api(value = "authController", description = "마니또 가입 및 로그인 API & 인증 API")
class AuthController(private val authService: AuthService, private val userService: UserService) {

    @ApiOperation(value = "회원가입 코드 발행", response = Response::class)
    @ApiResponses(value = [
        ApiResponse(code = 202, message = "Accepted"),
        ApiResponse(code = 5004, message = "메일 전송을 실패했습니다.")
    ])
    @PostMapping(value = ["/issue/code"])
    fun issueCode(@RequestBody body: IssueCodeRequest): Response<Unit> {
        authService.sendAuthCodeToEmail(body.receiverInfo)
        return Response(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.reasonPhrase)
    }

    @ApiOperation(value = "회원가입 코드 인증", response = VerifyCodeInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK"),
        ApiResponse(code = 401, message = "인증 코드가 맞지 않습니다.")
    ])
    @PostMapping(value = ["/verify/code"])
    fun verifyCode(@RequestBody body: VerifyCodeRequest): Response<RegisterToken> {
        val registerToken = authService.verifyCode(body.code)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, RegisterToken(registerToken))
    }

    @ApiOperation(value = "회원가입", response = SignUpInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Created", response = SignUpInfo::class),
        ApiResponse(code = 401, message = "인증 되지 않은 사용자입니다.", response = ErrorInfo::class)
    ])
    @PostMapping(value = ["/sign-up"])
    fun signUp(@ApiParam(value = "회원가입하기 위해서 필요한 토큰") @RequestHeader("X-Register-Token") registerToken: String,
               @ApiParam(value = "회원 가입 Body") @RequestBody body: SignUpDto): Response<UserDto> {
        if (!authService.isRegisterable(registerToken)) {
            throw NonAuthorizationException(errorMsg = "인증 되지 않은 사용자입니다.")
        }
        val userDto = userService.signUp(body)

        return Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase, userDto)
    }

    @ApiOperation(value = "로그인",response = SignInInfo::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "OK"),
        ApiResponse(code = 401, message = "비밀번호가 틀렸습니다."),
        ApiResponse(code = 404, message = "이메일을 찾을 수 없습니다.")
    ])
    @PostMapping(value = ["/sign-in"])
    fun signIn(@ApiParam(value = "로그인 Body") @RequestBody body: SignInDto): Response<AccessToken> {
        val accessToken = userService.signIn(body)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, accessToken)
    }
}