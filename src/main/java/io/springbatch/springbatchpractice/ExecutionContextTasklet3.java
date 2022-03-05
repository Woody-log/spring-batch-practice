package io.springbatch.springbatchpractice;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet3 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Object name = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("name");

        /**
         * 첫 번째 실행에선 런타임 에러가 발생하지만, 두 번째 실행에선 에러가 발생되지 않는다.
         * 두 번째 실행에선 실패한 step3 부터 실행되며, 첫번째 실행에서 name 값이 저장되기 때문에 조건문이 실행되지 않는다.
         * 첫 번째 실행 시 BATCH_JOB_EXECUTION_CONTEXT 에 name : user1이 저장되고 두 번째 실행 시 DB 에서 해당 내용을 불러와 조건문이 실행되지 않는다.
         */
        if (name == null) {
            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("name", "user1");
            throw new RuntimeException("step has failed");
        }

        return RepeatStatus.FINISHED;
    }
}
