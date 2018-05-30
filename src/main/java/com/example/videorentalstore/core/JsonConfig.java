package com.example.videorentalstore.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.Locale;

/**
 * JSON serialization and deserialization for {@link Money}.
 *
 * @author Sasa Bolic
 */
@Configuration
public class JsonConfig {

    @Bean
    public Module moneyModule() {
        return new MoneyModule();
    }

    public static class MoneyModule extends SimpleModule {

        public MoneyModule() {

            addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
            addValueInstantiator(MonetaryAmount.class, new MoneyInstantiator());
        }

        static class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount> {

            MonetaryAmountSerializer() {
                super(MonetaryAmount.class);
            }

            @Override
            public void serialize(MonetaryAmount value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                if (value != null) {
                    jgen.writeString(MonetaryFormats.getAmountFormat(Locale.ROOT).format(value));
                } else {
                    jgen.writeNull();
                }
            }
        }

        static class MoneyInstantiator extends ValueInstantiator {

            @Override
            public String getValueTypeDesc() {
                return MonetaryAmount.class.toString();
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext context, String value) {
                return Money.parse(value, MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()));
            }
        }
    }
}
