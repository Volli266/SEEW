package structure

import JSON

/**
 * Status des Tasks (Tickets)
 */
enum class TaskStatus(private val json: String) {
    READY(JSON.TASK_READY),
    IN_PROGRESS(JSON.TASK_IN_PROGRESS),
    IN_REVIEW(JSON.TASK_IN_REVIEW),
    TESTING(JSON.TASK_TESTING),
    FINISHED(JSON.TASK_FINISHED);

    /**
     * @return JSON-Repräsentation des Status
     */
    fun toJSON(): String {
        return json
    }
}
