package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.ReviewForFrontDto;
import uz.com.onlineshop.model.dto.response.UserForFrontDto;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.FileService;
import uz.com.onlineshop.service.ReviewService;
import uz.com.onlineshop.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {


    private final UserService userService;
    private final ReviewService reviewService;
    private final FileService fileService;


    @GetMapping("/get-user-by-id/{id}")
    public StandardResponse<UserForFrontDto> getById(
            @PathVariable UUID id
    ) {
        return userService.getById(id);
    }


    @DeleteMapping("/{id}/delete-by-id")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ) {
        return userService.deleteUser(id, principal);
    }


    @PutMapping("/forgot-password")
    public StandardResponse<String> forgotPassword(
            @RequestParam String code,
            @RequestParam String newPassword,
            Principal principal
    ) {
        return userService.forgotPassword(code, newPassword, principal);
    }


    @PutMapping("/{id}/assign-to-admin")
    @PreAuthorize("hasRole('OWNER')")
    public StandardResponse<UserForFrontDto> assignToAdmin(
            @PathVariable UUID id,
            Principal principal
    ) {
        return userService.assignToAdmin(id, principal);
    }


    @PutMapping("/update-profile/{id}")
    public StandardResponse<UserForFrontDto> updateProfile(
            @PathVariable UUID id,
            Principal principal,
            @RequestBody UserDto userDto
    ) {
        return userService.updateProfile(id, userDto, principal);
    }


    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public Page<UserForFrontDto> getUsers(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAll(pageable);
    }


    @PutMapping("/{id}/user-blocked")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<String> userBlocked(
            @PathVariable UUID id,
            Principal principal
    ) {
        return userService.userBlocked(id, principal);
    }


    @PutMapping("/{id}/user-activated")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<String> userActivated(
            @PathVariable UUID id,
            Principal principal
    ) {
        return userService.userActivated(id, principal);
    }


    @PutMapping("/get-all-user-by-status")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public Page<UserForFrontDto> getUsersByStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUserByStatus(pageable, status);
    }


    @GetMapping("/{id}/get-user-reviews")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public Page<ReviewForFrontDto> getUsersReviews(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.findReviewsByUserId(pageable, id);
    }


    @GetMapping("/{id}/get-pdf")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public ResponseEntity<byte[]> getUserPdf(@PathVariable UUID id) {
        try {
            byte[] pdf = fileService.generateUserPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/get-all-excel")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public ResponseEntity<byte[]> getAllUsersExcel() {
        List<UserEntity> users = userService.getAllUsersToExcel();
        try {
            byte[] excelFile = fileService.generateUsersExcel(users);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "users.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @DeleteMapping("/multi-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> multiDelete(
            @RequestBody List<String> id,
            Principal principal
    ) {
        return userService.multiDeleteById(id, principal);
    }


    @GetMapping("/search-by-number")
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<UserForFrontDto> searchByNumber(
            @RequestParam String number
    ) {
        return userService.searchUserByNumber(number);
    }


    @GetMapping("/search-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserForFrontDto> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchByName(name, pageable);
    }
}
