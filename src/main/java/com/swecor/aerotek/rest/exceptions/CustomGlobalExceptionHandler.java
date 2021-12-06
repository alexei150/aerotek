package com.swecor.aerotek.rest.exceptions;

import com.swecor.aerotek.rest.exceptions.calculation.FanToAcousticCalculateNotFound;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.calculation.ParseZiehlAbeggApiResponseFailed;
import com.swecor.aerotek.rest.exceptions.calculation.UnselectedThickness;
import com.swecor.aerotek.rest.exceptions.crm.ProjectNotFound;
import com.swecor.aerotek.rest.exceptions.library.*;
import com.swecor.aerotek.rest.exceptions.media.FileIsEmpty;
import com.swecor.aerotek.rest.exceptions.media.FileIsMissingFromTheHardDisk;
import com.swecor.aerotek.rest.exceptions.media.MediaContentIdIsAbsent;
import com.swecor.aerotek.rest.exceptions.selection.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // дескриптор ошибок для @Valid
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status, WebRequest request) {

        Map body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        // Получить все ошибки
        List errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    // Обработка Exceptions не связанных с @Valid
    @ExceptionHandler(LogicalException.class)
    protected ResponseEntity<AwesomeException> handleLogicalException(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                "Логическая ошибка : " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailIsNotUnique.class)
    protected ResponseEntity<CustomGlobalExceptionHandler.AwesomeException> handleEmailIsNotUnique() {
        return new ResponseEntity<>(new CustomGlobalExceptionHandler.AwesomeException(
                "Пользователь с таким email уже существует"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleEmailIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет пользователя с таким email"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleParameterIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Параметр с таким наименованием уже существует"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleParameterIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет параметра с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionTypeIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleSectionTypeIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Тип секции не уникален"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionTypeIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleSectionTypeIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет типа секции с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementTypeIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleElementTypeIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Тип элемента не уникален"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementTypeIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleElementTypeIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет типа элемента с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleElementIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Элемент не уникален"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementDrawingCodeIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleElementCodeIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Код чертежа элемента не уникален"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleIsElementIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет элемента с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleSectionIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Секция не уникалена"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleSectionIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет секции с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionDrawingCodeIsNotUnique.class)
    protected ResponseEntity<AwesomeException> handleSectionDrawingCodeIsNotUnique() {
        return new ResponseEntity<>(new AwesomeException(
                "Код чертежа секции не уникален"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MediaContentIdIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleMediaContentIdIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет файла с таким id"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileIsEmpty.class)
    protected ResponseEntity<AwesomeException> handleFileIsEmpty() {
        return new ResponseEntity<>(new AwesomeException(
                "Файл пуст"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileIsMissingFromTheHardDisk.class)
    protected ResponseEntity<AwesomeException> handleFileIsMissingFromTheHardDisk() {
        return new ResponseEntity<>(new AwesomeException(
                "Файл отсутствует на жестком диске"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InstallationToSelectionIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleInstallationToSelectionIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет подбираемой установки с таким ID"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZiehlAbeggSessionIdException.class)
    protected ResponseEntity<AwesomeException> handleZiehlAbeggSessionIdException() {
        return new ResponseEntity<>(new AwesomeException(
                "Исключение при Аутентификации в ZiehlAbegg"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZiehlAbeggSessionIdNullException.class)
    protected ResponseEntity<AwesomeException> handleZiehlAbeggSessionIdNullException() {
        return new ResponseEntity<>(new AwesomeException(
                "При Аутентификации в FanSelect API получен null"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZiehlAbeggSelectionIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleZiehlAbeggSelectionIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Отсутствуют вентиляторы ZiehlAbegg с желаемыми характеристиками"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterByNameNotFound.class)
    protected ResponseEntity<AwesomeException> handleParameterByNameNotFound(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                "Не найден параметр с именем '" + ex.getMessage() + " при поиске введенных пользователем значений"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterValueIsNull.class)
    protected ResponseEntity<AwesomeException> handleParameterValueIsNull(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedAirValveNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedSectionNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Воздушный клапан' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedEmptySectionNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedEmptySectionNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Пустая секция' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedMufflerNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedMufflerNotFount() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Шумоглушитель' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedFanFreeWellNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedFanFreeWellNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Вентилятор свободное колесо' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedPocketFilterNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedPocketFilterNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Карманный фильтр' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedCassetteFilterNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedCassetteFilterNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Касетный фильтр' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InstallationFlowIsEmpty.class)
    protected ResponseEntity<AwesomeException> handleInstallationFlowIsEmpty() {
        return new ResponseEntity<>(new AwesomeException(
                "Не выбран приток или вытяжка установки"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FanToAcousticCalculateNotFound.class)
    protected ResponseEntity<AwesomeException> handleFanToAcousticCalculateNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "При расчете Акустических характеристик не обнаружен Вентилятор на потоке"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParseZiehlAbeggApiResponseFailed.class)
    protected ResponseEntity<AwesomeException> handleParseZiehlAbeggApiResponseFailed() {
        return new ResponseEntity<>(new AwesomeException(
                "Ошибка при форматировани ответа ZiehlAbdegg API"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnselectedThickness.class)
    protected ResponseEntity<AwesomeException> handleUnselectedThickness() {
        return new ResponseEntity<>(new AwesomeException(
                "Не указано опциональное оснащение"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InstallationIsNotCalculated.class)
    protected ResponseEntity<AwesomeException> handleInstallationIsNotCalculated() {
        return new ResponseEntity<>(new AwesomeException(
                "Установка не посчитана"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DefaultBuilderIsAbsent.class)
    protected ResponseEntity<AwesomeException> handleDefaultBuilderIsAbsent() {
        return new ResponseEntity<>(new AwesomeException(
                "Шаблон установки не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ListOfVariableElementsIsEmpty.class)
    protected ResponseEntity<AwesomeException> handleListOfVariableElementsIsEmpty(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                "У типа секции '" + ex.getMessage() + "' отсутствуют вариативные элементы для подбора"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoenEsdDllException.class)
    protected ResponseEntity<AwesomeException> handleRoenEsdDllException(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                "Исключение при подборе теплообменника RoenEstDll: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KlingenburgDllException.class)
    protected ResponseEntity<AwesomeException> handleKlingenburgDllException(RuntimeException ex) {
        return new ResponseEntity<>(new AwesomeException(
                "Исключение при подборе роторного рекуператора Klingenburg: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelectedHeatExchangerNotFound.class)
    protected ResponseEntity<AwesomeException> handleSelectedHeatExchangerNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "В базе данных отсутствует секция 'Теплообменник' с указанными критериями поиска"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectNotFound.class)
    protected ResponseEntity<AwesomeException> handleProjectNotFound() {
        return new ResponseEntity<>(new AwesomeException(
                "Нет проекта с таким id"), HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    static class AwesomeException {
        private String message;
    }
}
