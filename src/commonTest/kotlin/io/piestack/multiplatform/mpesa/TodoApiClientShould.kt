package io.piestack.multiplatform.mpesa

import io.piestack.multiplatform.mpesa.common.api.TodoApiMockEngine
import io.piestack.multiplatform.mpesa.common.coroutines.runTest
import io.piestack.multiplatform.mpesa.common.helpers.JsName
import io.piestack.multiplatform.mpesa.common.responses.*
import io.piestack.multiplatform.mpesa.error.ItemNotFoundError
import io.piestack.multiplatform.mpesa.error.UnknownError
import io.piestack.multiplatform.mpesa.model.Task
import kotlinx.serialization.UnstableDefault
import kotlin.test.*

class TodoApiClientShould {
    companion object {
        private const val ANY_TASK_ID = "1"

        private const val ALL_TASK_SEGMENT = "/todos"
        private const val TASK_SEGMENT = "/todos/$ANY_TASK_ID"

        private val ANY_TASK = Task(1, 1, "delectus aut autem", false)
    }

    private val todoApiMockEngine = TodoApiMockEngine()

    @UnstableDefault
    @JsName("SendAcceptHeaderTest")
    @Test
    fun `send accept header`() = runTest {
        val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, getTasksResponse())

        apiClient.getAllTasks()

        todoApiMockEngine.verifyRequestContainsHeader("Accept", "application/json")
    }

    @UnstableDefault
    @JsName("SendRequestWithGetHttpVerbGettingAllTaskTest")
    @Test
    fun `send request with get http verb getting all task`() = runTest {
        val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, getTasksResponse())

        apiClient.getAllTasks()

        todoApiMockEngine.verifyGetRequest()
    }

    @UnstableDefault
    @JsName("ReturnTasksAndParsesItProperlyTest")
    @Test
    fun `return tasks and parses it properly`() = runTest {
        val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, getTasksResponse())

        val tasksResponse = apiClient.getAllTasks()

        tasksResponse.fold(
            { left -> fail("Should return right but was left: $left") },
            { right ->
                assertEquals(4, right.size.toLong())
                assertTaskContainsExpectedValues(right[0])
            })
    }

    @UnstableDefault
    @JsName("ReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return http error 500 if server response internal server error getting all task`() =
        runTest {
            val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, httpStatusCode = 500)

            val tasksResponse = apiClient.getAllTasks()

            tasksResponse.fold(
                { left -> assertEquals(UnknownError(500), left) },
                { right -> fail("Should return left but was right: $right") })
        }

    @JsName("yuReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send request with get http verb getting getting by id`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, getTaskByIdResponse())

        apiClient.getTasksById(ANY_TASK_ID)

        todoApiMockEngine.verifyGetRequest()
    }

    @JsName("neReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return task and parses it properly getting by id`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, getTaskByIdResponse())

        val taskResponse = apiClient.getTasksById(ANY_TASK_ID)

        taskResponse.fold(
            { left -> fail("Should return right but was left: $left") },
            { right ->
                assertTaskContainsExpectedValues(right)
            })
    }

    @JsName("zeReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return item not found error if there is no task with the passed id`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 404)

        val taskResponse = apiClient.getTasksById(ANY_TASK_ID)

        taskResponse.fold(
            { left -> assertEquals(ItemNotFoundError, left) },
            { right -> fail("Should return left but was right: $right") })
    }

    @JsName("zaReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return http error 500 if server response internal server error getting task by id`() =
        runTest {
            val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 500)

            val taskResponse = apiClient.getTasksById(ANY_TASK_ID)

            taskResponse.fold(
                { left -> assertEquals(UnknownError(500), left) },
                { right -> fail("Should return left but was right: $right") })
        }

    @JsName("baReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send request with post http verb adding a new task`() = runTest {
        val apiClient =
            givenAMockTodoApiClient(ALL_TASK_SEGMENT, addTaskResponse(), httpStatusCode = 201)

        apiClient.addTask(ANY_TASK)

        todoApiMockEngine.verifyPostRequest()
    }

    @JsName("beReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send the correct body adding a new task`() = runTest {
        val apiClient =
            givenAMockTodoApiClient(ALL_TASK_SEGMENT, addTaskResponse(), httpStatusCode = 201)

        apiClient.addTask(ANY_TASK)

        todoApiMockEngine.verifyRequestBody(addTaskRequest())
    }

    @JsName("biReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return task and parses it properly adding a new task`() = runTest {
        val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, addTaskResponse())

        val taskResponse = apiClient.addTask(ANY_TASK)

        taskResponse.fold(
            { left -> fail("Should return right but was left: $left") },
            { right ->
                assertTaskContainsExpectedValues(right)
            })
    }

    @JsName("boReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return http error 500 if server response internal server error adding a new task`() =
        runTest {
            val apiClient = givenAMockTodoApiClient(ALL_TASK_SEGMENT, httpStatusCode = 500)

            val taskResponse = apiClient.addTask(ANY_TASK)

            taskResponse.fold(
                { left -> assertEquals(UnknownError(500), left) },
                { right -> fail("Should return left but was right: $right") })
        }

    @JsName("buReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send request with put http verb updating a task`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, updateTaskResponse())

        apiClient.updateTask(ANY_TASK)

        todoApiMockEngine.verifyPutRequest()
    }

    @JsName("caReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send the correct body updating a new task`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, updateTaskResponse())

        apiClient.updateTask(ANY_TASK)

        todoApiMockEngine.verifyRequestBody(updateTaskRequest())
    }

    @JsName("ceReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return task and parses it properly updating a new task`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, addTaskResponse())

        val taskResponse = apiClient.updateTask(ANY_TASK)

        taskResponse.fold(
            { left -> fail("Should return right but was left: $left") },
            { right ->
                assertTaskContainsExpectedValues(right)
            })
    }

    @JsName("ciReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return item not found error if there is no task updating it`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 404)

        val taskResponse = apiClient.updateTask(ANY_TASK)

        taskResponse.fold(
            { left -> assertEquals(ItemNotFoundError, left) },
            { right -> fail("Should return left but was right: $right") })
    }

    @JsName("coReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return http error 500 if server response internal server error updating a task`() =
        runTest {
            val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 500)

            val taskResponse = apiClient.updateTask(ANY_TASK)

            taskResponse.fold(
                { left -> assertEquals(UnknownError(500), left) },
                { right -> fail("Should return left but was right: $right") })
        }

    @JsName("cuReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `send request with delete http verb deleting a task`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 200)

        apiClient.deleteTask(ANY_TASK_ID)

        todoApiMockEngine.verifyDeleteRequest()
    }

    @JsName("daReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return item not found error if there is no task deleting it`() = runTest {
        val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 404)

        val taskResponse = apiClient.deleteTask(ANY_TASK_ID)

        taskResponse.fold(
            { left -> assertEquals(ItemNotFoundError, left) },
            { right -> fail("Should return left but was right: $right") })
    }

    @JsName("deReturnHttpError500IfServerResponseInternalServerErrorGettingAllTaskTest")
    @Test
    fun `return http error 500 if server response internal server error deleting a task`() =
        runTest {
            val apiClient = givenAMockTodoApiClient(TASK_SEGMENT, httpStatusCode = 500)

            val taskResponse = apiClient.deleteTask(ANY_TASK_ID)

            taskResponse.fold(
                { left -> assertEquals(UnknownError(500), left) },
                { right -> fail("Should return left but was right: $right") })
        }

    private fun assertTaskContainsExpectedValues(task: Task?) {
        assertTrue(task != null)
        assertEquals(task.id, 1)
        assertEquals(task.userId, 1)
        assertEquals(task.title, "delectus aut autem")
        assertFalse(task.completed)
    }

    private fun givenAMockTodoApiClient(
        endpointSegment: String,
        responseBody: String = "",
        httpStatusCode: Int = 200
    ): TodoApiClient {
        todoApiMockEngine.enqueueMockResponse(endpointSegment, responseBody, httpStatusCode)

        return TodoApiClient(todoApiMockEngine.get().engine)
    }
}