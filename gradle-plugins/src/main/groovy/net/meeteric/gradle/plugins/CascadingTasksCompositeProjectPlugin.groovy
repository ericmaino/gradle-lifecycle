package net.meeteric.gradle.plugins

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.logging.Logger
import org.slf4j.LoggerFactory

class CascadingTasksCompositeProjectPlugin extends CascadingTasksPluginBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CascadingTasksCompositeProjectPlugin)

    @Override
    void registerTask(String taskName, String taskGroup, String taskDescription, Project currentProject) {
        findOrCreateTask(currentProject.tasks, taskName, taskGroup, taskDescription).configure {
            project.gradle.includedBuilds.each  {
                TaskReference foundTask = project.gradle.includedBuild(it.name).task(":${taskName}")

                if (foundTask) {
                    LOGGER.debug("Mapping task '${taskName}' from '${currentProject.name}' to '${it.name}'")
                    dependsOn foundTask
                }
            }
        }
    }

}
