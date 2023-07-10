package com.dvaren.bill.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginVo {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "code不能为空")
    private String code;

    @NotBlank(message = "昵称")
    private String avatar;

    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
