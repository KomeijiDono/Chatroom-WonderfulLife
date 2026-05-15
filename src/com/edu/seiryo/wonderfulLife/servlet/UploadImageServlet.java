package com.edu.seiryo.wonderfulLife.servlet;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * 图片上传接口
 * POST /uploadImage — 接收 multipart 图片文件，保存到 /images/upload/ 并返回访问 URL
 * 限制：单文件最大 5MB，请求最大 10MB
 */
@WebServlet("/uploadImage")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class UploadImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        // 检查上传文件是否为空
        Part part = request.getPart("image");
        if (part == null || part.getSize() == 0) {
            response.getWriter().write("{\"success\":false,\"msg\":\"请选择图片\"}");
            return;
        }

        // 使用 UUID 生成唯一文件名，防止重名覆盖
        String fileName = extractFileName(part);
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + ext;

        // 确保上传目录存在，不存在则创建
        String uploadPath = getServletContext().getRealPath("/images/upload");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        part.write(uploadPath + File.separator + newFileName);

        String imageUrl = request.getContextPath() + "/images/upload/" + newFileName;
        response.getWriter().write("{\"success\":true,\"url\":\"" + imageUrl + "\"}");
    }

    private String extractFileName(Part part) {
        String header = part.getHeader("content-disposition");
        for (String cd : header.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf("=") + 2, cd.length() - 1);
            }
        }
        return "unknown.jpg";
    }
}
