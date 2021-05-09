<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>Barista page</h5>
    <form action="/barista" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <div class="card w-50 my-3">
            <ul class="list-group list-group-flush">
                <#list orders as order>
                    <li class="list-group-item">
                        <div class="form-row">
                            <div class="col-auto">
                                <b>${order.id}</b>
                            </div>
                            <div class="col-sm-3">
                                ${order.menu.drink.title}
                            </div>
                            <div class="col-sm-3">
                                ${order.menu.capacity.symbol} - ${order.menu.capacity.value} мл.
                            </div>
                            <div class="col-sm-3">
                                ${order.user.name}
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary" name="${order.id}">Готово</button>
                            </div>
                        </div>
                    </li>
                <#else>
                    No Orders :(
                </#list>
            </ul>
        </div>
    </form>
</@c.page>