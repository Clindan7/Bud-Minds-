package com.innovaturelabs.buddymanagement.service.impl;

import com.innovaturelabs.buddymanagement.BuddyManagementApplication;
import com.innovaturelabs.buddymanagement.entity.Technology;
import com.innovaturelabs.buddymanagement.form.TechnologyForm;
import com.innovaturelabs.buddymanagement.repository.TechnologyRepository;
import com.innovaturelabs.buddymanagement.util.LanguageUtil;
import com.innovaturelabs.buddymanagement.view.TechnologyView;
import com.innovaturelabs.buddymanagement.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.ArgumentMatchers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ContextConfiguration
@SpringBootTest(classes = BuddyManagementApplication.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class TechnologyServiceImplTest {

    @InjectMocks
    @Autowired
    TechnologyServiceImpl technologyService;

    @MockBean
    private TechnologyRepository technologyRepository;

    @Autowired
    LanguageUtil languageUtil;

    @Test
     void getsTechnologyList() {
        String search="c++";
        Technology technology = new Technology(1);
        technology.setTechnologyId(1);
        technology.setTechnologyName("c++");
        technology.setStatus(Technology.Status.ACTIVE.value);
        doThrow(NullPointerException.class).when(technologyRepository).findByTechnologyId(1);
        doThrow(NullPointerException.class).when(technologyRepository).findByTechnologyName(search);
        doThrow(NullPointerException.class).when(technologyRepository).findAllByStatus();
        assertThrows(NullPointerException.class,()->technologyService.listTechnology());
        verify(technologyRepository).findAllByStatus();
    }


    @Test
    void getsTechnologyList_() {
        List<Technology> technologyList = new ArrayList<>();
        Technology technology1 = new Technology();
        technology1.setTechnologyId(1);
        technology1.setTechnologyName("Java");
        technology1.setStatus(Technology.Status.ACTIVE.value);
        technologyList.add(technology1);
        Technology technology2 = new Technology();
        technology2.setTechnologyId(2);
        technology2.setTechnologyName("Python");
        technology2.setStatus(Technology.Status.ACTIVE.value);
        technologyList.add(technology2);
        when(technologyRepository.findAllByStatus()).thenReturn(technologyList);
        List<Technology> returnedList = technologyService.listTechnology();
        assertEquals(2, returnedList.size());
        assertEquals("Java", returnedList.get(0).getTechnologyName());
        assertEquals("Python", returnedList.get(1).getTechnologyName());
    }

    @Test
    void getsTechnologyList_2() {
        String search="c++";
        Technology technology = new Technology(1);
        technology.setTechnologyId(1);
        technology.setTechnologyName("c++");
        technology.setStatus(Technology.Status.ACTIVE.value);
        List<Technology> technologyList=new ArrayList<>();
        technologyList.add(technology);
        doReturn(List.of(technology)).when(technologyRepository).findAllByStatus();
        doThrow(BadRequestException.class).when(technologyRepository).findAllByStatus();
        assertThrows(BadRequestException.class,()->technologyService.listTechnology());
        verify(technologyRepository).findAllByStatus();
    }

    @Test
    void technologyAdd() {
        TechnologyForm technologyForm = new TechnologyForm();
        technologyForm.setTechnologyName("c++");
        Technology technology = new Technology(1);
        technology.setTechnologyId(1);
        technology.setTechnologyName("c++");
        technology.setStatus((byte) 1);
        LocalDateTime dt = LocalDateTime.now();
        technology.setCreateDate(dt);
        technology.setUpdateDate(dt);
        doReturn(technology).when(technologyRepository).save(ArgumentMatchers.any());
        doThrow(BadRequestException.class).when(technologyRepository).findByTechnologyName("ploi");
        TechnologyView technologyView = new TechnologyView(technology);
        assertEquals(technologyView.getTechnologyId(), technologyService.technologyAdd(technologyForm).getTechnologyId());
    }

    @Test
    void testTechnologyAddThrowsBadRequestException() {
        TechnologyForm form = new TechnologyForm();
        when(technologyRepository.findByTechnologyName(form.getTechnologyName())).thenReturn(Optional.of(new Technology("Java")));
        assertThrows(BadRequestException.class, () -> technologyService.technologyAdd(form));
        verify(technologyRepository, times(1)).findByTechnologyName(form.getTechnologyName());
        verify(technologyRepository, times(0)).save(any(Technology.class));
    }

    @Test
    void testTechnologyDeleteThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> technologyService.technologyDelete(1));
        verify(technologyRepository, times(0)).save(any(Technology.class));
    }

    @Test
    void testTechnologyDelete() throws BadRequestException {
        Integer technologyId = 1;
        Technology technology = new Technology("Java");
        technology.setStatus(Technology.Status.ACTIVE.value);
        when(technologyRepository.findByTechnologyId(technologyId)).thenReturn(Optional.of(technology));
        when(technologyRepository.save(any(Technology.class))).thenReturn(technology);
        technologyService.technologyDelete(technologyId);
        assertEquals(Technology.Status.INACTIVE.value, technology.getStatus());
        assertNotNull(technology.getUpdateDate());
        verify(technologyRepository, times(1)).findByTechnologyId(technologyId);
        verify(technologyRepository, times(1)).save(any(Technology.class));
    }

    @Test
    void testTechnologyDeleteThrowsBadRequestExceptionForInactiveTechnology() {
        Integer technologyId = 1;
        Technology technology = new Technology("Java");
        technology.setStatus(Technology.Status.INACTIVE.value);
        when(technologyRepository.findByTechnologyId(technologyId)).thenReturn(Optional.of(technology));
        assertThrows(BadRequestException.class, () -> technologyService.technologyDelete(technologyId));
        verify(technologyRepository, times(1)).findByTechnologyId(technologyId);
        verify(technologyRepository, times(0)).save(any(Technology.class));
    }


}