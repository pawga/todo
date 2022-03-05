package com.pawga.todo.data.models

import javax.persistence.*

@Entity
@Table(name = "todo")
class TodoJpa(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Column(name = "title")
    var title: String,

    @Column(name = "detail")
    var detail: String,

    @Column(name = "done")
    var done: Boolean
) {
}