import com.innovaturelabs.buddymanagement.entity.JoinerBatch;
import com.innovaturelabs.buddymanagement.entity.JoinerGroup;
import com.innovaturelabs.buddymanagement.form.JoinerBatchForm;
import com.innovaturelabs.buddymanagement.form.MentorForm;
import com.innovaturelabs.buddymanagement.repository.JoinerBatchRepository;
import com.innovaturelabs.buddymanagement.repository.JoinerGroupRepository;
import com.innovaturelabs.buddymanagement.repository.MentorRepository;
import com.innovaturelabs.buddymanagement.service.MentorService;
import com.innovaturelabs.buddymanagement.service.impl.JoinerBatchServiceImpl;
import com.innovaturelabs.buddymanagement.view.JoinerBatchView;
import com.innovaturelabs.buddymanagement.view.JoinerGroupViewLite;
import com.innovaturelabs.buddymanagement.view.UserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuddyManagementApplicationTest {

      @Mock
      private JoinerBatchRepository joinerBatchRepository;

      private JoinerGroupRepository joinerGroupRepository;
      private MentorRepository mentorRepository;
      @Autowired
      private MentorService mentorService;
      @InjectMocks
      private JoinerBatchServiceImpl joinerBatchService;

      @BeforeEach
      void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);
            joinerBatchRepository = mock(JoinerBatchRepository.class);
            joinerGroupRepository = mock(JoinerGroupRepository.class);
            mentorRepository = mock(MentorRepository.class);
      }

      @Test
      void findAllBatch() {

            // Given
            JoinerBatch joinerBatch = new JoinerBatch("August 2022");

            when(joinerBatchRepository.findAllByStatusOrderByCreateDateDesc(
                    JoinerBatch.Status.ACTIVE.value
            )).thenReturn(List.of(joinerBatch));
            Collection<JoinerBatchView> batches = StreamSupport.stream(joinerBatchRepository.
                            findAllByStatusOrderByCreateDateDesc(JoinerBatch.Status.ACTIVE.value).spliterator(), false)
                    .map(JoinerBatchView::new)
                    .collect(Collectors.toList());
            // Then
            assertEquals(List.of(joinerBatch).size(), batches.size());
            verify(this.joinerBatchRepository).findAllByStatusOrderByCreateDateDesc(JoinerBatch.Status.ACTIVE.value);
      }

      @Test
      void findByJoinerBatchIdTest() {

            // Given
            JoinerBatch joinerBatch = new JoinerBatch(1);
            joinerBatch.setJoinerBatchName("August 2022");

            when(joinerBatchRepository.findByJoinerBatchIdAndStatus(anyInt(),
                    eq(JoinerBatch.Status.ACTIVE.value))).thenReturn(Optional.of(joinerBatch));
            Optional<JoinerBatch> returnedJoinerBatch = this.joinerBatchRepository.findByJoinerBatchIdAndStatus(1,JoinerBatch.Status.ACTIVE.value);

            // Then
            assertTrue(returnedJoinerBatch.isPresent());
            verify(this.joinerBatchRepository).findByJoinerBatchIdAndStatus(1,JoinerBatch.Status.ACTIVE.value);
      }
      @Test
      void createBatchTest() {
            // Given
            JoinerBatch joinerBatch = new JoinerBatch(1);
            joinerBatch.setJoinerBatchName("August 2022");

            when(joinerBatchRepository.save(joinerBatch)).thenReturn(joinerBatch);
            JoinerBatch returnedJoinerBatch = this.joinerBatchRepository.save(joinerBatch);

            // Then
            verify(this.joinerBatchRepository).save(joinerBatch);
      }

      @Test
      void findBatchBySearchTest() {

            // Given
            JoinerBatch joinerBatch = new JoinerBatch("August 2022");

            when(joinerBatchRepository.findAllByStatusOrderByCreateDateDesc(
                    JoinerBatch.Status.ACTIVE.value
            )).thenReturn(List.of(joinerBatch));
            Collection<JoinerBatchView> batches = StreamSupport.stream(joinerBatchRepository.
                            findAllByStatusOrderByCreateDateDesc(JoinerBatch.Status.ACTIVE.value).spliterator(), false)
                    .map(JoinerBatchView::new)
                    .collect(Collectors.toList());
            // Then
            assertEquals(List.of(joinerBatch).size(), batches.size());
            verify(this.joinerBatchRepository).findAllByStatusOrderByCreateDateDesc(JoinerBatch.Status.ACTIVE.value);
      }

      @Test
      void createGroupTest() {
            // Given
            JoinerBatch joinerBatch = new JoinerBatch(1);
            joinerBatch.setJoinerBatchName("August 2022");

            JoinerGroup joinerGroup=new JoinerGroup("G1",joinerBatch);

            when(joinerGroupRepository.save(joinerGroup)).thenReturn(joinerGroup);
            JoinerGroup returnedJoinerBatch = this.joinerGroupRepository.save(joinerGroup);

            // Then
            verify(this.joinerGroupRepository).save(joinerGroup);
      }
      @Test
      void findAllGroup() {

            // Given
          JoinerBatch joinerBatch = new JoinerBatch(1);
          joinerBatch.setJoinerBatchName("August 2022");
          JoinerGroup joinerGroup=new JoinerGroup("G1",joinerBatch);
          joinerGroup.setJoinerGroupId(1);


            when(joinerGroupRepository.findAllByStatusOrderByCreateDateDesc(
                    JoinerGroup.Status.ACTIVE.value
            )).thenReturn(List.of(joinerGroup));
            List<JoinerGroupViewLite> groups = StreamSupport.stream(joinerGroupRepository.
                            findAllByStatusOrderByCreateDateDesc(JoinerGroup.Status.ACTIVE.value).spliterator(), false)
                    .map(JoinerGroupViewLite::new)
                    .collect(Collectors.toList());
            // Then
            assertEquals(List.of(joinerGroup).size(), groups.size());
            verify(this.joinerGroupRepository).findAllByStatusOrderByCreateDateDesc(JoinerGroup.Status.ACTIVE.value);
      }

      @Test
      void findGroupByJoinerBatchIdTest() {

            // Given
            JoinerBatch joinerBatch = new JoinerBatch(1);
            joinerBatch.setJoinerBatchName("August 2022");
            JoinerGroup joinerGroup=new JoinerGroup("G1",joinerBatch);
            joinerGroup.setJoinerGroupId(1);

            when(joinerGroupRepository.findByJoinerGroupIdAndStatus(anyInt(),
                    eq(JoinerBatch.Status.ACTIVE.value))).thenReturn(Optional.of(joinerGroup));
            Optional<JoinerGroup> returnedJoinerGroup = this.joinerGroupRepository.findByJoinerGroupIdAndStatus(1,JoinerGroup.Status.ACTIVE.value);

            // Then
            assertTrue(returnedJoinerGroup.isPresent());
            verify(this.joinerGroupRepository).findByJoinerGroupIdAndStatus(1,JoinerGroup.Status.ACTIVE.value);
      }

      @Test
      void findGroupBySearchTest() {

            // Given
            JoinerBatch joinerBatch = new JoinerBatch(1);
            joinerBatch.setJoinerBatchName("August 2022");
            JoinerGroup joinerGroup=new JoinerGroup("G1",joinerBatch);
            joinerGroup.setJoinerGroupId(1);

            when(joinerGroupRepository.findAllByStatusOrderByCreateDateDesc(
                    JoinerGroup.Status.ACTIVE.value
            )).thenReturn(List.of(joinerGroup));
            List<JoinerGroupViewLite> groups = StreamSupport.stream(joinerGroupRepository.
                            findAllByStatusOrderByCreateDateDesc(JoinerGroup.Status.ACTIVE.value).spliterator(), false)
                    .map(JoinerGroupViewLite::new)
                    .collect(Collectors.toList());
            // Then
            assertEquals(List.of(joinerGroup).size(), groups.size());
            verify(this.joinerGroupRepository).findAllByStatusOrderByCreateDateDesc(JoinerGroup.Status.ACTIVE.value);
      }






}

