package com.billlog.rest.api.controller.v1;

import com.billlog.rest.api.advice.exception.CCommonDeleteFailedException;
import com.billlog.rest.api.advice.exception.CCommonUpdateFailedException;
import com.billlog.rest.api.advice.exception.CCommonWriteFailedException;
import com.billlog.rest.api.advice.exception.CInfoArticleNotFoundException;
import com.billlog.rest.api.controller.FcmPushUtils;
import com.billlog.rest.api.controller.common.FileUploadController;
import com.billlog.rest.api.mapper.DeviceMapper;
import com.billlog.rest.api.mapper.FileMapper;
import com.billlog.rest.api.mapper.InfoMapper;
import com.billlog.rest.api.model.FoodInfo;
import com.billlog.rest.api.model.response.CommonResult;
import com.billlog.rest.api.model.response.ListResult;
import com.billlog.rest.api.model.response.SingleResult;
import com.billlog.rest.api.service.AndroidFCMPushNotificationsService;
import com.billlog.rest.api.service.ResponseService;
import com.billlog.rest.api.util.CustomUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Api(tags = {"3. FoodInfo"})
@RestController
@RequestMapping(value = "/v1")
public class FoodInfoController {

    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private final ResponseService responseService; // 결과를 처리할 Service
    @Autowired
    private final AndroidFCMPushNotificationsService androidFCMPushNotificationsService;
    private final FileUploadController fileUploadController; // 결과를 처리할 Service

    public FoodInfoController(ResponseService responseService, AndroidFCMPushNotificationsService androidFCMPushNotificationsService, FileUploadController fileUploadController) {
        this.responseService = responseService;
        this.androidFCMPushNotificationsService = androidFCMPushNotificationsService;
        this.fileUploadController = fileUploadController;
    }

    //유효한 Jwt토큰을 설정해야만 User 리소스를 사용할 수 있도록 Header에 X-AUTH-TOKEN을 인자로 받도록 선언합니다.
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "정보성 게시글 전체 목록 조회", notes = "class, party, club 구분없이 모든 목록을 조회온다.")
    @GetMapping("/infos/{type}")
    public ListResult<FoodInfo> getAll(@ApiParam(value = "정보성글의 타입", required = true)@PathVariable String type,
                                       @ApiParam(value = "페이지 번호", required = true)@RequestParam int page,
                                       @ApiParam(value = "표시 글의 개수", required = true)@RequestParam int count) {

        //MariaDB LIMIT 페이지 계산식
        page = (page - 1) * count;

        return responseService.getListResult(infoMapper.getInfoArticleAll(type, page, count));
    }

    //유효한 Jwt토큰을 설정해야만 User 리소스를 사용할 수 있도록 Header에 X-AUTH-TOKEN을 인자로 받도록 선언합니다.
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", dataType = "String", paramType = "header")
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "정보게시글 조회", notes = "info_idx를 이용해 정보 게시글를 조회한다.")
    @GetMapping("/info/{info_idx}")
    public SingleResult<FoodInfo> getInfoArticleByIdx(@ApiParam(value = "정보게시글 IDX", required = true)@PathVariable int info_idx,
                                                      @ApiParam(value = "정보게시글 타입", required = true)@RequestParam String type) {

        FoodInfo info = infoMapper.getInfoArticleByIdx(info_idx, type);

        if(CustomUtils.isEmpty(info)){
            throw new CInfoArticleNotFoundException();
        }else{

            if( !CustomUtils.isEmpty(info.getAtt_file_id()) ){
                //해당 글에 등록된 파일이 있으면 가져와서 셋팅한다.
                info.setInfoFiles(fileMapper.getFilesByIdx(info.getAtt_file_id()));
            }

            return responseService.getSingleResult(info);
        }
    }

    // info 정보성 게시글 등록
    //유효한 Jwt토큰을 설정해야만 User 리소스를 사용할 수 있도록 Header에 X-AUTH-TOKEN을 인자로 받도록 선언합니다.
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "Content-Type", value = "폼데이터 속성", required = true, dataType = "String", paramType = "header", defaultValue = "multipart/form-data")
    })
    @ApiOperation(value = "정보게시글 등록", notes = "정보성 게시물을 작성한다.")
    @PostMapping("/info")

    public CommonResult createInfoArticle(@ApiParam(value = "정보게시글 작성 Object", required = true) FoodInfo foodInfo,
                                          @RequestPart(value="files", required = false) MultipartFile[] files,
                                          @ApiParam(value = "저장 디렉토리 명", required = true) String dir) throws UnsupportedEncodingException {

        int result = 0;

        if(!CustomUtils.isEmpty(foodInfo)) {

            if(!CustomUtils.isEmpty(files)) {
                int file_manage_id = fileUploadController.returnFileManageId(0); // 새롭게 등록이 되는 경우 0

                //해당 글에 파일 관리 번호를 지정해준다.
                foodInfo.setAtt_file_id(file_manage_id);

                fileUploadController.uploadMultipleFiles(files, dir, file_manage_id);
            }
            result = infoMapper.createInfoArticle(foodInfo);
        }

        if(result == 1) {

            // 인포 정보가 등록 되고 그 글과 같은 도시 지역을 관심지역으로 설정한 사용자에게 푸쉬를 날린다.
            String articleType = "";
            if("P".equals(foodInfo.getType())){
                articleType = "파티";
            }else if("C".equals(foodInfo.getType())){
                articleType = "강습";
            }else if("F".equals(foodInfo.getType())){
                articleType = "페스티벌";
            }else{
                articleType = "";
            }
            //등록된 도시 번호로 동일한 회원 ID값을 가져온다.
            List<String> tokenList = deviceMapper.findMsgReciverUsersByCity(foodInfo.getCity());

            if( tokenList.size() > 0 ) {
                String title = "SALSAPAN";
                String content = "관심 지역의 " + articleType + " 정보가 등록되었습니다. \n 확인 후 " + articleType + " 함께하세요.";

                title = URLEncoder.encode(title, "UTF-8");
                content = URLEncoder.encode(content, "UTF-8");

                Map<String, Object> fcm = new HashMap<>();
                fcm.put("title", title);
                fcm.put("body", content);
                fcm.put("tokens", tokenList);

                HttpEntity<String> request = FcmPushUtils.createFcmMessageTargetToCity(fcm);
                CompletableFuture<String> pushNotification = androidFCMPushNotificationsService.send(request);
                CompletableFuture.allOf(pushNotification).join();
            }

            return responseService.getSuccessResult();
        }else{
            throw new CCommonWriteFailedException();
        }
    }

    //정보게시글 idx를 통해 해당 정보 게시글 내용 변경
    //유효한 Jwt토큰을 설정해야만 User 리소스를 사용할 수 있도록 Header에 X-AUTH-TOKEN을 인자로 받도록 선언합니다.
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "정보게시글 수정", notes = "정보성 게시물 수정한다.")
    @PutMapping("/info/{info_idx}")
    public CommonResult modifyInfoArticleByIdx(@ApiParam(value = "정보게시글 IDX", required = true)@PathVariable int info_idx,
                                               @ApiParam(value = "정보게시글 작성 Object", required = true) FoodInfo foodInfo){

        //정보게시글 인덱스 번호가 없을 경우 메시지와 함께 예외처리
        if("".equals(foodInfo.getInfo_idx())){
            throw new CInfoArticleNotFoundException();
        }

        int result = infoMapper.modifyInfoArticleByIdx(foodInfo);

        if(result == 1) {
            return responseService.getSuccessResult();
        }else{
            throw new CCommonUpdateFailedException();
        }
    }

    // info index로 info 게시글 삭제(use_yn = 'N')
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "정보 게시글 삭제", notes = "정보성게시글 삭제(사용여부 변경)")
    @DeleteMapping("/info/{info_idx}")
    public CommonResult deleteSalsaUserByIdx(@ApiParam(value = "정보게시글 IDX", required = true)@PathVariable int info_idx){

        if("".equals(info_idx)){ //값이 없을 경우
            throw new CInfoArticleNotFoundException();
        }

        Boolean result = infoMapper.deleteInfoArticleByIdx(info_idx);

        if(result) {
            return responseService.getSuccessResult();
        }else{
            throw new CCommonDeleteFailedException();
        }
    }
}
