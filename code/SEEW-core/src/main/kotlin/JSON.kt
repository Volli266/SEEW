/**
 * JSON-Konstanten
 */
object JSON {

    // Attribute um die JSON-Struktur zu laden
    // todo: Attribute in JSON-Struktur zu Gunsten
    //       der Einheitlichkeit in die unten Aufgeführten
    //       umbenennen
    const val L_NAME = "name"
    const val L_TIME = "time"
    const val L_REQUIRE = "require"
    const val L_TASKS = "tasks"
    const val L_TASKS_ALWAYS = "tasks_always"
    const val L_TASKS_CHOOSE = "tasks_choose"
    const val L_DESCR = "descr"
    const val L_REWARD = "reward"
    const val L_DINO_R = "dino_r"
    const val L_DINO_A = "dino_a"
    const val L_OP = "op"
    const val L_VAL = "val"
    const val L_VALL = "vall"
    const val L_VALH = "valh"
    const val L_CONTAIN = "contain"
    const val L_ID = "id"
    const val L_POOL_1 = "Pool1"
    const val L_POOL_2 = "Pool2"
    const val L_GE = "ge"
    const val L_LE = "le"
    const val L_EQ = "eq"
    const val L_BT = "bt"
    const val L_EXTR = "extr"

    // JSON attribute names ///////////////////////////////

    //// RPC
    const val FUNCTION = "fun"
    const val PARAMS = "params"

    //// Login
    const val USERNAME = "username"
    const val PASSWORD = "password"

    //// Error
    const val ERROR = "error"

    // Grouping
    const val GROUPS = "groups"
    const val GROUP_NAME = "groupName"
    const val GROUP_COLOR = "groupColor"
    const val GROUP_MEMBERS = "groupMembers"

    //// Simulation
    ////// Dino
    const val DINO_NAME = "dinoName"
    const val DINO_COLOR = "dinoColor"
    const val DINO_LOGIC = "dinoLogic"
    const val DINO_COURAGE = "dinoCourage"
    const val DINO_WEIGHT = "dinoWeight"
    const val DINO_AGILITY = "dinoAgility"
    const val DINO_STRENGTH = "dinoStrength"
    const val DINO_SIZE = "dinoSize"
    const val DINO_REACTION = "dinoReaction"
    const val DINO_SOCIAL = "dinoSocial"
    const val DINO_DEXTERITY = "dinoDexterity"
    const val DINO_FRONT = "dinoFront"
    const val DINO_BACK = "dinoBack"
    const val DINO_TAIL = "dinoTail"
    const val DINO_ABILITIES = "dinoAbilities"

    ////// Developers
    const val DEVELOPER_ONE = "developerOne"
    const val DEVELOPER_TWO = "developerTwo"
    const val DEVELOPER_ID = "developerId"
    const val DEVELOPER_FORENAME = "developerForename"
    const val DEVELOPER_SURNAME = "developerSurname"
    const val DEVELOPER_SOCIAL = "developerSocial"
    const val DEVELOPER_LOGIC = "developerLogic"
    const val DEVELOPER_COURAGE = "developerCourage"
    const val DEVELOPER_REACTION = "developerReaction"
    const val DEVELOPER_AGILITY = "developerAgility"
    const val DEVELOPER_DEXTERITY = "developerDexterity"
    const val DEVELOPER_STRENGTH = "developerStrength"
    const val DEVELOPER_WEIGHT = "developerWeight"
    const val DEVELOPER_SIZE = "developerSize"
    const val DEVELOPER_BODY = "developerBody"

    ////// ProductBacklog
    const val EPIC = "epic"
    const val EPIC_ORDER = "epicOrder"
    const val EPIC_PRIORITY = "epicPriority"

    ////// SprintBacklog
    const val STORY_POOL_ONE = "storyPoolOne"
    const val STORY_POOL_TWO = "storyPoolTwo"
    const val STORY_NAME = "storyName"
    const val STORY_DESCRIPTION = "storyDescription"
    const val STORY_REQUIREMENTS = "storyRequirements"
    const val STORY_REWARDS = "storyRewards"
    const val STORY_SPRINT = "storySprint"
    const val STORY_ESTIMATION = "storyEstimation"
    const val STORY_TASKS = "storyTasks"
    const val STORY_ID = "storyId"
    const val DECISIONS = "decisions"
    const val DECISION_NAME = "decisionName"
    const val DECISION_ID = "decisionId"
    const val CHOSEN_DECISION = "chosenDecision"
    const val CHOSEN_STORY_ONE = "chosenStoryOne"
    const val CHOSEN_STORY_TWO = "chosenStoryTwo"
    const val SPRINT_TIME = "sprintTime"

    ////// Estimation
    const val ESTIMATION_VALUE = "estimationValue"
    const val ESTIMATION_STORY = "estimationStory"

    ////// SprintProgress
    const val TASK_NAME = "taskName"
    const val TASK_TIME = "taskTime"
    const val TASK_REQUIREMENTS = "taskRequirements"
    const val TASK_DEVELOPER_ONE = "taskDeveloperOne"
    const val TASK_DEVELOPER_TWO = "taskDeveloperTwo"
    const val TASK_TEST = "taskTest"
    const val TASK_SPRINT = "taskSprint"
    const val TASK_COMPLETION = "taskCompletion"
    const val TASK_FAILURE_PROBABILITY = "taskFailureProbability"
    const val TASK_STATUS = "taskStatus"
    const val TASK_READY = "taskReady"
    const val TASK_IN_PROGRESS = "taskInProgress"
    const val TASK_IN_REVIEW = "taskInReview"
    const val TASK_TESTING = "taskTesting"
    const val TASK_FINISHED = "taskFinished"
    const val TASK_DINO_REL = "taskDinoRel"
    const val TASK_DINO_ABS = "taskDinoAbs"
    const val TASK_ID = "taskId"
    const val CURRENT_TASKS = "currentTasks"
    const val CURRENT_SPRINT_TIME = "currentSprintTime"
    const val SPRINT_DAILY_TIME = "sprintDailyTime"
    const val SPRINT_DAILY_STATUS = "sprintDailyStatus"

    ////// ProzessZwischensicht
    const val CURRENT_PHASE = "currentPhase"
    const val CURRENT_SPRINT = "currentSprint"

    ////// Navigation
    const val CURRENT_TIME = "currentTime"

    ////// Evaluation
    const val EPICS = "epics"
    const val CHOSEN_DECISION_EVAL = "chosenDecisionEval"
    const val CHOSEN_STORY_ONE_EVAL = "chosenStoryOneEval"
    const val CHOSEN_STORY_TWO_EVAL = "chosenStoryTwoEval"
    const val INVOLVED_DEVELOPERS = "involvedDevelopers"
    const val LEFTOVER_TASKS = "leftoverTasks"
    const val SUCCESS = "success"
    const val TESTED = "tested"
    const val DAILY_DONE_EVAL = "dailyDoneEval"
    const val SPRINT_TIME_EVAL = "sprintTimeEval"

    // Remote function names //////////////////////////////

    //// Login
    ////// Server -> Client
    const val CONFIRM_LOGIN = "confirmLogin"
    const val NOTIFY_LOGIN = "notifyLogin"
    const val RETURN_TO_LOGIN = "returnToLogin"
    const val ALREADY_STARTED = "alreadyStarted"
    ////// Client -> Server
    const val LOGIN = "login"
    const val REGISTER = "register"

    //// Grouping
    ////// Server -> Client
    const val UPDATE_GROUP_LIST = "updateGroupList"
    const val NOTIFY_GROUP_OVERVIEW = "notifyGroupOverview"
    const val SHOW_INTRO = "showIntro"
    ////// Client -> Server
    const val REQUEST_GROUP_LIST = "requestGroupList"
    const val CREATE_GROUP = "createGroup"
    const val JOIN_GROUP = "joinGroup"
    const val LEAVE_GROUP = "leaveGroup"
    const val READY_FOR_INTRO = "readyForIntro"

    //// Intro
    ////// Server -> Client
    const val SHOW_SIMULATION = "showSimulation"
    const val NOTIFY_INTRO = "notifyIntro"
    ////// Client -> Server
    const val READY_FOR_SIMULATION = "readyForSimulation"

    //// Simulation
    ////// Server -> Client
    const val UPDATE_DINO = "updateDino"
    const val UPDATE_DEVELOPERS = "updateDevelopers"
    const val UPDATE_TIME = "updateTime"
    const val UPDATE_SPRINT_OVERVIEW = "updateSprintOverview"
    const val UPDATE_PRODUCT_BACKLOG = "updateProductBacklog"
    const val UPDATE_SPRINT_BACKLOG = "updateSprintBacklog"
    const val UPDATE_ESTIMATION = "updateEstimation"
    const val UPDATE_SPRINT_PROGRESS = "updateSprintProgress"
    const val UPDATE_SPRINT_REVIEW = "updateSprintReview"
    const val UPDATE_SPRINT_RETRO = "updateSprintRetro"
    const val UPDATE_EVALUATION = "updateEvaluation"
    const val NOTIFY_SPRINT_OVERVIEW = "notifySprintOverview"
    ////// Client -> Server
    const val REQUEST_UPDATE_ALL = "requestUpdateAll"
    const val REQUEST_UPDATE_DINO = "requestUpdateDino"
    const val REQUEST_UPDATE_DEVELOPERS = "requestUpdateDevelopers"
    const val REQUEST_UPDATE_SPRINT_OVERVIEW = "requestUpdateSprintOverview"
    const val REQUEST_UPDATE_PRODUCT_BACKLOG = "requestUpdateProductBacklog"
    const val REQUEST_UPDATE_SPRINT_BACKLOG = "requestUpdateSprintBacklog"
    const val REQUEST_UPDATE_ESTIMATION = "requestUpdateEstimation"
    const val REQUEST_UPDATE_SPRINT_PROGRESS = "requestUpdateSprintProgress"
    const val REQUEST_UPDATE_SPRINT_REVIEW = "requestUpdateSprintReview"
    const val REQUEST_UPDATE_SPRINT_RETRO = "requestUpdateSprintRetro"
    const val REQUEST_UPDATE_EVALUATION = "requestUpdateEvaluation"
    const val CONFIRM_PHASE = "confirmPhase"
    const val SET_EPIC_ONE = "setEpicOne"
    const val SET_EPIC_TWO = "setEpicTwo"
    const val SET_EPIC_THREE = "setEpicThree"
    const val SET_DECISION = "setDecision"
    const val SET_STORY_ONE = "setStoryOne"
    const val SET_STORY_TWO = "setStoryTwo"
    const val SET_SPRINT_TIME = "setSprintTime"
    const val ESTIMATE_STORY_ONE = "estimateStoryOne"
    const val ESTIMATE_STORY_TWO = "estimateStoryTwo"
    const val START_SPRINT_DAILY = "startSprintDaily"
    const val SET_TASK_READY = "setTaskReady"
    const val SET_TASK_IN_PROGRESS = "setTaskInProgress"
    const val SET_TASK_DEVELOPER_ONE = "setTaskDeveloperOne"
    const val SET_TASK_DEVELOPER_TWO = "setTaskDeveloperTwo"
    const val SET_TASK_TEST = "setTaskTest"
    const val SET_TASK_NOT_TEST = "setTaskNotTest"
    const val START_SPRINT_REVIEW = "startSprintReview"
}
