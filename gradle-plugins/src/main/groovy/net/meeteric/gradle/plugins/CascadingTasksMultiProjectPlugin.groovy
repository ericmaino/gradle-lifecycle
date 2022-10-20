package net.meeteric.gradle.plugins

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.logging.Logger
import org.slf4j.LoggerFactory

class CascadingTasksMultiProjectPlugin extends CascadingTasksPluginBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CascadingTasksMultiProjectPlugin)

    @Override
    void registerTask(String taskName, String taskGroup, String taskDescription, Project currentProject) {
        TaskContainer tasks = currentProject.tasks
        findOrCreateTask(tasks, taskName, taskGroup, taskDescription).configure {
            currentProject.subprojects {
                def foundTask = project.tasks.findByPath(taskName)

                if (foundTask) {
                    LOGGER.info("Mapping task '${taskName}' from '${currentProject.name}' to '${project.name}'")
                    dependsOn foundTask
                } else {
                    LOGGER.quiet("Skipping task '${taskName}' from '${currentProject.name}' to '${project.name}'")
                }
            }
        }
    }

}
