package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.entity.Comment;
import com.innovaturelabs.buddymanagement.entity.Session;
import com.innovaturelabs.buddymanagement.entity.User;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import com.innovaturelabs.buddymanagement.form.SessionCommentForm;
import com.innovaturelabs.buddymanagement.repository.CommentRepository;
import com.innovaturelabs.buddymanagement.repository.SessionRepository;
import com.innovaturelabs.buddymanagement.repository.UserRepository;
import com.innovaturelabs.buddymanagement.security.util.SecurityUtil;
import com.innovaturelabs.buddymanagement.service.CommentService;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.util.Pager;
import com.innovaturelabs.buddymanagement.view.CommentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommentServiceImpl implements CommentService {

    private static final String PERMISSION_NOT_ALLOWED="permission.not.allowed";

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void addComment(SessionCommentForm form,Integer sessionId){
        Session session=sessionRepository.findByIdAndStatusNot(sessionId,Session.Status.INACTIVE.value).orElseThrow(()->new BadRequestException(languageUtil.getTranslatedText("session.not.found",null,"en")));
        User user=userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        if(user.getUserRole()==4) {
            if (user.getJoinerGroup() != null) {
                if (session.getjoinerGroupId().getJoinerGroupId() != user.getJoinerGroup().getJoinerGroupId()) {
                    throw new BadRequestException(languageUtil.getTranslatedText(PERMISSION_NOT_ALLOWED,null,"en"));
                }
            }else {
                throw new BadRequestException(languageUtil.getTranslatedText(PERMISSION_NOT_ALLOWED,null,"en"));
            }
        } else if (!session.getTrainerId().getUserId().equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException(languageUtil.getTranslatedText(PERMISSION_NOT_ALLOWED,null,"en"));
        }
        if(session.getStatus()!=3){
            throw new BadRequestException(languageUtil.getTranslatedText("session.cannot.comment",null,"en"));
        }
        commentRepository.save(new Comment(session,user,form.getComment(),user.getUserRole()));
    }

    public Pager<CommentView> listComment(Integer sessionId, Integer page, Integer limit){
        sessionRepository.findByIdAndStatusNot(sessionId,Session.Status.INACTIVE.value).orElseThrow(()->new BadRequestException(languageUtil.getTranslatedText("session.not.found",null,"en")));
        List<CommentView> commentView;
        Pager<CommentView> commentViewPager;

        commentView= StreamSupport.stream(commentRepository.findBySessionSessionId(sessionId, PageRequest.of(page-1,limit)).spliterator(),false).map(CommentView::new).collect(Collectors.toList());

        commentViewPager=new Pager<>(limit,commentRepository.countBySessionSessionId(sessionId),page);
        commentViewPager.setResult(commentView);
        return commentViewPager;

    }
}
