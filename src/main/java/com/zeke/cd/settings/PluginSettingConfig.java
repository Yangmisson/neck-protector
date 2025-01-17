package com.zeke.cd.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.ComponentWithBrowseButton.BrowseFolderActionListener;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.zeke.cd.notify.PluginDefaultConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

/**
 * 插件设置页面的配置参数
 *
 * @author King.Z
 * @version 1.0
 * @since 2019-04-29
 */
public interface PluginSettingConfig {

    /**
     * 支持的图片格式
     */
    List<String> MIME_OF_IMAGE = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");

    /**
     * 支持的图片格式的拼接字符串，用于弹窗提示
     */
    String IMAGE_EXTENSION_LIST_STR = String.join("、", MIME_OF_IMAGE);

    /**
     * 图片选择器的描述对象
     */
    FileChooserDescriptor IMAGE_FILE_CHOOSER = new FileChooserDescriptor(true, false, false, false, false, false) {
        @Override
        public void validateSelectedFiles(VirtualFile[] files) throws Exception {
            super.validateSelectedFiles(files);
            for (VirtualFile file : files) {
                if (!MIME_OF_IMAGE.contains(file.getExtension())) {
                    throw new IllegalArgumentException("请确保上传的是 " + IMAGE_EXTENSION_LIST_STR + " 文件，然后重试");
                }
            }
        }
    };

     /**
     * 图片选择器的监听事件
     */
    static BrowseFolderActionListener newBrowseFolderActionListener(TextFieldWithBrowseButton textField) {
        return new BrowseFolderActionListener<JTextField>("image / 图片 URL", "Choose the picture you like / 选择你喜欢的图片",
                textField, null,
                PluginSettingConfig.IMAGE_FILE_CHOOSER,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT) {
            @NotNull
            @Override
            protected String chosenFileToResultingText(@NotNull VirtualFile chosenFile) {
                try {
                    // 选择图片时，返回文件完整的 URL 而不仅仅是 Path
                    return VfsUtil.toUri(chosenFile).toURL().toString();
                } catch (MalformedURLException e) {
                    return PluginDefaultConfig.IMAGE_URL;
                }
            }
        };
    }
}
