package bd.pro.saumik.shrnkit.common.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface VisitorService {

    UUID resolveVisitor(
            HttpServletRequest request,
            HttpServletResponse response
    );

}