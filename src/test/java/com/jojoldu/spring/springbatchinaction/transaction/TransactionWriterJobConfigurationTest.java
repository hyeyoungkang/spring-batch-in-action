package com.jojoldu.spring.springbatchinaction.transaction;

import com.jojoldu.spring.springbatchinaction.TestBatchConfig;
import com.jojoldu.spring.springbatchinaction.persistwriter.JpaMergeWriterJobConfiguration;
import com.jojoldu.spring.springbatchinaction.transaction.domain.Student;
import com.jojoldu.spring.springbatchinaction.transaction.domain.Teacher;
import com.jojoldu.spring.springbatchinaction.transaction.domain.TeacherRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 20/08/2018
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes={TransactionWriterJobConfiguration.class, TestBatchConfig.class})
public class TransactionWriterJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TeacherRepository teacherRepository;

    @SuppressWarnings("Duplicates")
    @Test
    public void Writer트랜잭션테스트() throws Exception {
        //given
        for(long i=0;i<10;i++) {
            String teacherName = i + "선생님";
            Teacher teacher = new Teacher(teacherName, "수학");
            Student student = new Student(teacherName+"의 제자");
            teacher.addStudent(student);
            teacherRepository.save(teacher);
        }

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString("version", "1");

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters());

        //then
        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
    }

}
