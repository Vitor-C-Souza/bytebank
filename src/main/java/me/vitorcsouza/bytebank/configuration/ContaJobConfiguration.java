package me.vitorcsouza.bytebank.configuration;
import me.vitorcsouza.bytebank.model.Conta;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ContaJobConfiguration {
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job job(Step firstStep, JobRepository jobRepository){
        return new JobBuilder("gerar-contas", jobRepository)
                .start(firstStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step firstStep(ItemReader<Conta> reader, ItemWriter<Conta> writer, JobRepository jobRepository){
        return new StepBuilder("first-step", jobRepository)
                .<Conta, Conta>chunk(200, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Conta> reader(){
        return new FlatFileItemReaderBuilder<Conta>()
                .name("leitura-csv")
                .resource(new FileSystemResource("files/dados_ficticios.csv"))
                .comments("Nome|CPF|Agência|Conta|Valor|Mês de Referência")
                .delimited()
                .delimiter("|")
                .names("nome", "cpf", "agencia", "conta", "valor", "referencia")
                .fieldSetMapper(new ContaMapper())
                .build();
    }

    @Bean
    public ItemWriter<Conta> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Conta>()
                .dataSource(dataSource)
                .sql(
                        "INSERT INTO conta_tb (nome, cpf, agencia, numero_conta, valor, mes_referencia) VALUES " + "(:nome, :cpf, :agencia, :numeroConta, :valor, :mesReferencia)"
                )
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();

    }
}
