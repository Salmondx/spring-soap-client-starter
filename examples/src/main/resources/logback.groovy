import ch.qos.logback.classic.encoder.PatternLayoutEncoder

import java.nio.charset.Charset

import static ch.qos.logback.classic.Level.INFO

def appenderList = ["CONSOLE"]


logger('org.springframework', OFF)
logger('org.springframework.boot', OFF)
logger('org.springframework.web.servlet', OFF)
logger('org.springframework.security.web', OFF)
logger('org.springframework.context.support', OFF)
logger('org.apache.cxf.services', INFO)

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName("UTF-8")
        pattern = "%-4relative %d %-5level [ %t ] %-55logger{13} | %m %n"
    }
}

root(INFO, appenderList)
