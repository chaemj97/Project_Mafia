package com.ssafy.mafia.Controller;

import com.ssafy.mafia.Model.RankDto;
import com.ssafy.mafia.Service.RankService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RankController {

    private final RankService rankService;

    @GetMapping("/game/topRank")
    @ApiOperation(value = "전체 유저 랭크 정보", notes = "랭크 유저정보 list로 반환하기", response = List.class)
    public List searchTopRank () {
        return rankService.searchTopRank();
    }

    @PutMapping("/user/game/rank")
    @ApiOperation(value = "랭크정보 수정하기", notes = "자신의 승, 패, 점수 변경", response = RankDto.class)
    public RankDto changeRank(@RequestBody boolean result) {
        return rankService.changeRank(result);
    }

//    @PostMapping("/game/userRanl")
//    @ApiOperation(value = "이 정보를 가진 유저의 랭킹 보여주기", notes = "랭크 유저정보 list 반환하기", response = List.class)
//    public List searchUserRank (@RequestBody String username) {
//        return rankService.searchUserRank;
//    }
}
