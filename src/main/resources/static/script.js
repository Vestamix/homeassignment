const applicationForm = document.getElementById('applicationForm');
const statusSection = document.getElementById('statusSection');
const statusText = document.getElementById('statusText');
const offersSection = document.getElementById('offersSection');
const offerList = document.getElementById('offerList');
const confirmationSection = document.getElementById('confirmationSection');
const confirmationText = document.getElementById('confirmationText');
let applicationId;
let statusInterval;

        function pollStatusAndOffers(applicationId) {
            fetch('/api/application/' + applicationId)
                .then(response => response.json())
                .then(application => {
                    if (application.status === 'PROCESSED') {
                        clearInterval(statusInterval);
                        displayOffers(application.loanResponses);
                    }
                })
                .catch(error => {
                    console.error('Error polling application:', error);
                });
        }

        function displayOffers(loanResponses) {
            statusSection.style.display = 'none';
            offersSection.style.display = 'block';

            if (loanResponses.length === 0) {
                 offersSection.style.display = 'none';
                 noOffersSection.style.display = 'block';
            } else {
                loanResponses.forEach(loanResponse => {
                    const offerItem = document.createElement('li');
                    const offerDetailsList = document.createElement('ul');
                    offerDetailsList.className = 'offer-details-list';

                    const bankNameItem = document.createElement('li');
                    bankNameItem.innerText = loanResponse.bankName;
                    offerDetailsList.appendChild(bankNameItem);

                    const offerDetails = [
                            'Monthly Payment: ' + loanResponse.offer.monthlyPaymentAmount.toFixed(2),
                            'Total Repayment: ' + loanResponse.offer.totalRepaymentAmount.toFixed(2),
                            'Number of Payments: ' + loanResponse.offer.numberOfPayments,
                            'Annual Percentage Rate: ' + loanResponse.offer.annualPercentageRate.toFixed(2) + '%',
                            'First Repayment Date: ' + loanResponse.offer.firstRepaymentDate
                    ];

                    offerDetails.forEach(detail => {
                            const detailItem = document.createElement('li');
                            detailItem.innerText = detail;
                            offerDetailsList.appendChild(detailItem);
                    });

                    const offerButton = document.createElement('button');
                    offerButton.innerText = 'Select ' + loanResponse.bankName;
                    offerButton.addEventListener('click', () => {
                            confirmOffer(loanResponse.bankName);
                    });

                    offerItem.appendChild(offerDetailsList);
                    offerItem.appendChild(offerButton);
                    offerList.appendChild(offerItem);
                });
            }
        }

        function confirmOffer(bankName) {
            offersSection.style.display = 'none';
            confirmationSection.style.display = 'block';
            confirmationText.innerText = 'Thank you for choosing ' + bankName + ', you will receive money shortly.';
        }

        applicationForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const errorMessageElement = document.getElementById('errorMessage');
            errorMessageElement.textContent = '';

            const formData = new FormData(applicationForm);

            const jsonObject = {};
            formData.forEach((value, key) => {
                if (key === 'phoneNumber') {
                            value = '+' + value;
                }
                jsonObject[key] = value;
            });

            const jsonString = JSON.stringify(jsonObject);

            const xhr = new XMLHttpRequest();
            xhr.open('POST', '/api/application/submit', true);
            xhr.setRequestHeader('Content-Type', 'application/json');
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        applicationForm.style.display = 'none';
                        const applicationId = parseInt(xhr.responseText);
                        statusSection.style.display = 'block';
                        statusInterval = setInterval(() => {
                            pollStatusAndOffers(applicationId);
                        }, 5000);
                    } else {
                        console.error('Error submitting application:', xhr.statusText);
                        try {
                            const errorResponse = JSON.parse(xhr.responseText);
                            const errorMessage = errorResponse.message;
                            document.getElementById('errorMessage').textContent = errorMessage;
                        } catch (error) {
                            console.error('Error parsing error response:', error);
                        }
                        applicationForm.style.display = 'block';
                    }
                }
            };
            xhr.send(jsonString);
        });