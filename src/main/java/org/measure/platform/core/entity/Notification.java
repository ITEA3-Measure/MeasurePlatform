package org.measure.platform.core.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.measure.platform.core.api.entitys.enumeration.NotificationType;

/**
 * A Phase.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "notification_title", nullable = false)
    private String notificationTitle;

    @Column(name = "notification_content")
    private String notificationContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "is_validated")
    private Boolean isValidated = false;

    @Column(name = "notification_date")
    private ZonedDateTime notificationDate;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getNotificationDate() {
        return notificationDate;
    }

    public Notification notificationDate(ZonedDateTime notificationDate) {
        this.notificationDate = notificationDate;
        return this;
    }

    public void setNotificationDate(ZonedDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public Notification nNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
        return this;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public Notification notificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
        return this;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public Notification notificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Boolean isIsValidated() {
        return isValidated;
    }

    public Notification isValidated(Boolean isValidated) {
        this.isValidated = isValidated;
        return this;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public Project getProject() {
        return project;
    }

    public Notification project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification phase = (Notification) o;
        if (phase.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, phase.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notification" + "id=" + id + ", notificationTitle='" + notificationTitle + "'"
                        + ", notificationContent='" + notificationContent + "'}";
    }

}
