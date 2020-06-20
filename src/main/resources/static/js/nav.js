import {fetchCategories, fetchCategoriesDropdown, getLocale} from "./fetch-functions.js";

$("ul.navbar-nav li.dropdown-arch").hover(
  function () {
    $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeIn(500);
  },
  function () {
    $(this).find(".dropdown-menu").stop(true, true).delay(200).fadeOut(500);
  }
);

$("ul.navbar-nav")
  .find(".dropdown-arch")
  .click(function (e) {
    e.stopPropagation();
  });

$(document).ready(function () {
  $(".dropdown-arch").each(function (index, e) {
    console.log(index, e);
    $(this).hover(
      function () {
        console.log("hover -> in");
        $(this).removeClass("dropdown-out");
      },
      function () {
        $(this).addClass("dropdown-out");
        const that = $(this);
        setTimeout(function () {
          that.removeClass("dropdown-out");
          console.log("removed class");
        }, 2000);
        console.log("hover -> out");
      }
    );
  });

  document.querySelectorAll(".dropdown-arch").forEach(function (dropdown) {
    const currentLang = dropdown.querySelector(".dropdown-icon");
    const content = dropdown.querySelector(".dropdown-content-arch");
    let index = 0;
    if (currentLang) {
      currentLang.addEventListener("click", function (event) {
        console.log(event);
        if (index % 2 === 0) {
          dropdown.classList.toggle("expanded-dropdown-arch");
          dropdown.classList.remove("dropdown-out");
        } else {
          dropdown.classList.toggle("dropdown-out");
          dropdown.classList.remove("expanded-dropdown-arch");
          setTimeout(function () {
            dropdown.classList.remove("dropdown-out");
            console.log("removed class");
          }, 2000);
        }
        index++;
      });
    }
  });

  const selectDropdown = $("#categories-dropdown");
  fetchCategoriesDropdown(selectDropdown);
});
