import org.frienitto.manitto.SpringTestSupport
import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.UserRoomMap
import org.frienitto.manitto.dto.RoomJoinRequest
import org.frienitto.manitto.exception.InvalidStatusException
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.RoomRepository
import org.frienitto.manitto.repository.UserRepository
import org.frienitto.manitto.repository.UserRoomMapRepository
import org.frienitto.manitto.service.RoomService
import org.frienitto.manitto.service.UserRoomMapService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class UserRoomMapServiceTest : SpringTestSupport() {

    companion object {
        private const val USERNAME = "TEST-USER"
        private const val EMAIL = "TEST-EMAIL"
        private const val CODE = "TEST-CODE"
        private const val INVALID_CODE = "INVALID-CODE"
        private const val TITLE = "TEST-TITLE"
    }

    @Autowired
    private lateinit var userRoomMapRepository: UserRoomMapRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var roomRepository: RoomRepository
    @Autowired
    private lateinit var roomService: RoomService

    @BeforeEach
    fun setUp() {
        val mockUser = userRepository.save(mockUser())
        val mockRoom = roomRepository.save(mockRoom(mockUser))
        userRoomMapRepository.save(mockUserRoomMaps(mockUser, mockRoom))
    }

    @Test
    fun `입장 가능한 상태가 아닌 방에 입장을 시도 할 경우`() {
        //given
        val user = userRepository.findByEmail(EMAIL) ?: throw ResourceNotFoundException()
        val room = roomRepository.findByTitle(TITLE) ?: throw ResourceNotFoundException()
        room.matched()
        roomRepository.save(room)

        //when, then
        Assertions.assertThrows(InvalidStatusException::class.java) {
            roomService.joinRoomByTitle(user, RoomJoinRequest(TITLE, CODE))
        }
    }

    @Test
    fun `유효하지 않은 code를 이용하여 입장을 시도 할 경우`() {
        //given
        val user = userRepository.findByEmail(EMAIL) ?: throw ResourceNotFoundException()
        val room = roomRepository.findByTitle(TITLE) ?: throw ResourceNotFoundException()

        //when, then
        Assertions.assertThrows(NonAuthorizationException::class.java) {
            roomService.joinRoomByTitle(user, RoomJoinRequest(TITLE, INVALID_CODE))
        }
    }

    private fun mockUserRoomMaps(user: User, room: Room): UserRoomMap {
        return UserRoomMap.newUserRoomMap(room = room, user = user)
    }

    private fun mockUser(): User {
        return User.newUser(username = USERNAME, description = "", imageCode = 1, email = EMAIL, password = "")
    }

    private fun mockRoom(user: User): Room {
        return Room.newRoom(owner = user, code = CODE, title = TITLE, expiresDate = LocalDate.now().plusDays(1))
    }
}