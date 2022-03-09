package com.example.swipe.utility;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Logger {

    private static final Map<String, org.slf4j.Logger> LOG_MAP = new HashMap<>();

    private Logger() {
    }

    /**
     * Logs a message object with the {@link Level#DEBUG DEBUG} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message string to log.
     */
    public static void debug(Class<?> clazz, String message) {
        computeAndGetLogger(clazz).debug(message);
    }

    /**
     * Logs a message with parameters at the {@link Level#DEBUG DEBUG} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message. If the last argument is a Throwable and is NOT used up by a placeholder in
     *                the message pattern, it will be logged, including its stack trace!
     */
    public static void debug(Class<?> clazz, String message, Object... params) {
        computeAndGetLogger(clazz).debug(message, params);
    }

    /**
     * Logs a message with parameters which are only to be constructed if the logging level is the {@link Level#DEBUG
     * DEBUG} level.
     *
     * @param clazz          Class on which this message will be logged.
     * @param message        the message to log; the format depends on the message factory.
     * @param paramSuppliers An array of {@link Supplier}, which when called, produce the desired log message parameters.
     */
    public static void debug(Class<?> clazz, String message, Supplier<?>... paramSuppliers) {
        org.slf4j.Logger logger = computeAndGetLogger(clazz);
        if (logger.isDebugEnabled()) {
            logger.debug(message, Arrays.stream(paramSuppliers).map(Supplier::get).toArray());
        }
    }

    /**
     * Logs a message at the {@link Level#DEBUG DEBUG} level including the stack trace of the {@link Throwable}
     * <code>t</code> passed as parameter.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message object to log.
     * @param t       the exception to log, including its stack trace.
     */
    public static void debug(Class<?> clazz, String message, Throwable t) {
        computeAndGetLogger(clazz).debug(message, t);
    }

    /**
     * Logs a message object with the {@link Level#INFO INFO} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message string to log.
     */
    public static void info(Class<?> clazz, String message) {
        computeAndGetLogger(clazz).info(message);
    }

    /**
     * Logs a message with parameters at the {@link Level#INFO INFO} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message. If the last argument is a Throwable and is NOT used up by a placeholder in
     *                the message pattern, it will be logged, including its stack trace!
     */
    public static void info(Class<?> clazz, String message, Object... params) {
        computeAndGetLogger(clazz).info(message, params);
    }

    /**
     * Logs a message at the {@link Level#INFO INFO} level including the stack trace of the {@link Throwable}
     * <code>t</code> passed as parameter.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message object to log.
     * @param t       the exception to log, including its stack trace.
     */
    public static void info(Class<?> clazz, String message, Throwable t) {
        computeAndGetLogger(clazz).info(message, t);
    }

    /**
     * Logs a message object with the {@link Level#WARN WARN} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message string to log.
     */
    public static void warn(Class<?> clazz, String message) {
        computeAndGetLogger(clazz).warn(message);
    }

    /**
     * Logs a message with parameters at the {@link Level#WARN WARN} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message. If the last argument is a Throwable and is NOT used up by a placeholder in
     *                the message pattern, it will be logged, including its stack trace!
     */
    public static void warn(Class<?> clazz, String message, Object... params) {
        computeAndGetLogger(clazz).warn(message, params);
    }

    /**
     * Logs a message at the {@link Level#WARN WARN} level including the stack trace of the {@link Throwable}
     * <code>t</code> passed as parameter.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message object to log.
     * @param t       the exception to log, including its stack trace.
     */
    public static void warn(Class<?> clazz, String message, Throwable t) {
        computeAndGetLogger(clazz).warn(message, t);
    }

    /**
     * Logs a message object with the {@link Level#ERROR ERROR} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message string to log.
     */
    public static void error(Class<?> clazz, String message) {
        computeAndGetLogger(clazz).error(message);
    }

    /**
     * Logs a message with parameters at the {@link Level#ERROR ERROR} level.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message to log; the format depends on the message factory.
     * @param params  parameters to the message. If the last argument is a Throwable and is NOT used up by a placeholder in
     *                the message pattern, it will be logged, including its stack trace!
     */
    public static void error(Class<?> clazz, String message, Object... params) {
        computeAndGetLogger(clazz).error(message, params);
    }

    /**
     * Logs a message at the {@link Level#ERROR ERROR} level including the stack trace of the {@link Throwable}
     * <code>t</code> passed as parameter.
     *
     * @param clazz   Class on which this message will be logged.
     * @param message the message object to log.
     * @param t       the exception to log, including its stack trace.
     */
    public static void error(Class<?> clazz, String message, Throwable t) {
        computeAndGetLogger(clazz).error(message, t);
    }

    /**
     * Gets the {@link org.slf4j.Logger} from cached data for given class.
     *
     * @param clazz {@link Class}
     * @return Logger
     */
    private static org.slf4j.Logger computeAndGetLogger(Class<?> clazz) {
        if (!LOG_MAP.containsKey(clazz.getName())) {
            synchronized (Logger.class) {
                if (!LOG_MAP.containsKey(clazz.getName())) {
                    LOG_MAP.put(clazz.getName(), LoggerFactory.getLogger(clazz));
                }
            }
        }
        return LOG_MAP.get(clazz.getName());
    }

}
