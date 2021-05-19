<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Смены</h5>
    <a class="btn btn-primary" data-toggle="collapse" href="#addShiftCollapse"
       role="button" aria-expanded="false" aria-controls="addShiftCollapse">
        Добавить
    </a>
    <div class="collapse" id="addShiftCollapse">
        <div class="form-group w-50">
            <form action="/shift" method="post" class="row no-gutters">
                <div class="col-12">
                    <input class="form-control mb-2 mt-2" type="date" id="start" name="date"
                           value="2021-05-17" min="2021-01-01">
                </div>
                <div class="col-12">
                    <select class="form-control mb-2" name="barista" required>
                        <#list barista as b>
                            <option>${b.secondName} ${b.firstName} ${b.thirdName}</option>
                        </#list>
                    </select>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary">Добавить</button>
            </form>
        </div>
    </div>
    <div class="card w-50 my-3">
        <ul class="list-group list-group-flush">
            <#list shift as s>
                <li class="list-group-item">
                    <div class="form-row">
                        <div class="col-sm-3">
                            ${s.date}
                        </div>
                        <div class="col-sm-3">
                            ${s.barista.secondName} ${s.barista.firstName} ${s.barista.thirdName}
                        </div>
                        <div class="col-sm-3">
                            ${s.barista.cafe.address}
                        </div>
                        <form action="/shift/${s.id}/delete" method="post" class="col-auto">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger">Удалить</button>
                        </form>
                    </div>
                </li>
            <#else>
                No shifts :(
            </#list>
        </ul>
    </div>
</@c.page>