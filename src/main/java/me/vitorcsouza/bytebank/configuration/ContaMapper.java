package me.vitorcsouza.bytebank.configuration;

import me.vitorcsouza.bytebank.model.Conta;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class ContaMapper implements FieldSetMapper<Conta> {
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("MM/yy")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    @Override
    public Conta mapFieldSet(FieldSet fieldSet) throws BindException {
        Conta conta = new Conta(
                fieldSet.readString("nome"),
                fieldSet.readString("cpf"),
                fieldSet.readString("agencia"),
                fieldSet.readInt("conta"),
                fieldSet.readDouble("valor"),
                LocalDate.parse(fieldSet.readString("referencia"), dateTimeFormatter)
        );
        return conta;
    }
}
