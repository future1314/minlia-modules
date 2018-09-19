package com.minlia.module.todo.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.minlia.cloud.code.SystemCode;
import com.minlia.cloud.utils.ApiAssert;
import com.minlia.module.bible.body.BibleItemQueryRequestBody;
import com.minlia.module.bible.entity.BibleItem;
import com.minlia.module.bible.service.BibleItemService;
import com.minlia.module.common.util.NumberGenerator;
import com.minlia.module.todo.body.TodoCreateRequestBody;
import com.minlia.module.todo.body.TodoOperateRequestBody;
import com.minlia.module.todo.body.TodoQueryRequestBody;
import com.minlia.module.todo.body.TodoUpdateRequestBody;
import com.minlia.module.todo.constant.TodoCode;
import com.minlia.module.todo.entity.MyTodo;
import com.minlia.module.todo.enumeration.TodoStatus;
import com.minlia.module.todo.event.TodoCreateEvent;
import com.minlia.module.todo.mapper.TodoMapper;
import org.dozer.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private BibleItemService bibleItemService;

    private final static String BIBLE_TODO_TYPE = "TODO_TYPE";

    @Override
    @Transactional
    public MyTodo create(TodoCreateRequestBody requestBody) {
        BibleItem bibleItem = bibleItemService.queryOne(BibleItemQueryRequestBody.builder().parentCode(BIBLE_TODO_TYPE).code(requestBody.getType()).build());
        ApiAssert.notNull(bibleItem, TodoCode.Message.TYPE_NOT_EXISTS, requestBody.getType());

        MyTodo todo = mapper.map(requestBody, MyTodo.class);
        todo.setNumber(NumberGenerator.generatorByYMDHMSS("TD",1));
        todo.setStatus(TodoStatus.UNDONE);
        todoMapper.create(todo);

        TodoCreateEvent.onCreate(todo);
        return todo;
    }

    @Override
    @Transactional
    public MyTodo update(TodoUpdateRequestBody body) {
        MyTodo todo = this.queryByNumberAndNotNull(body.getNumber());
        BeanUtils.copyProperties(body, todo);
        todoMapper.update(todo);
        return todo;
    }

    @Override
    public MyTodo operate(TodoOperateRequestBody body) {
        MyTodo todo = this.queryByNumberAndNotNull(body.getNumber());
        switch (body.getOperate()) {
            case PEND:
                todo.setStatus(TodoStatus.PENDING);
                break;
            case FINISH:
                todo.setStatus(TodoStatus.FINISHED);
                break;
            case CANCEL:
                todo.setStatus(TodoStatus.CANCELED);
                break;
            default:
                break;
        }
        todo.setNotes(body.getNotes());
        todoMapper.update(todo);
        return todo;
    }

    @Override
    @Transactional
    public void delete(String number) {
        MyTodo todo = this.queryByNumberAndNotNull(number);
        todoMapper.delete(todo.getId());
    }

    @Override
    public MyTodo queryById(Long id) {
        return todoMapper.queryById(id);
    }

    @Override
    public MyTodo queryByNumber(String number) {
        return todoMapper.queryByNumber(number);
    }

    @Override
    public long count(TodoQueryRequestBody requestBody) {
        return todoMapper.count(requestBody);
    }

    private MyTodo queryByNumberAndNotNull(String number) {
        MyTodo todo = todoMapper.queryByNumber(number);
        ApiAssert.notNull(todo, SystemCode.Message.DATA_NOT_EXISTS);
        return todo;
    }

    @Override
    public List<MyTodo> queryList(TodoQueryRequestBody requestBody) {
        return todoMapper.queryList(requestBody);
    }

    @Override
    public PageInfo<MyTodo> queryPage(TodoQueryRequestBody requestBody, Pageable pageable) {
        return PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize()).doSelectPageInfo(() -> todoMapper.queryList(requestBody));
    }

}
